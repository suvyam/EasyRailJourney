package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation.CreateAllScheduleTrainStation;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation.ScheduleTrainStationCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation.ScheduleTrainStationDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleStation.ScheduleTrainStationUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory.ScheduleTrainStationHistory;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainStation;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Station;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainHistoryRepos.ScheduleTrainStationHistoryRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainStationRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.StationRepo;

import jakarta.transaction.Transactional;

@Service

// Rules:
// No duplicate sequence number
// Sequence starts from 1
// Sequence increases by exactly 1
// No gaps
// Each station should occur only once in a schedule

public class ScheduleTrainStationService {

    private final ScheduleTrainStationRepo scheduleTrainStationRepo;
    private final ScheduleTrainRepo scheduleTrainRepo;
    private final StationRepo stationRepo;
    private final ScheduleTrainStationHistoryRepo scheduleTrainStationHistoryRepo;

    public ScheduleTrainStationService(
            ScheduleTrainStationRepo scheduleTrainStationRepo,
            ScheduleTrainRepo scheduleTrainRepo,
            StationRepo stationRepo,
            ScheduleTrainStationHistoryRepo scheduleTrainStationHistoryRepo) {

        this.scheduleTrainStationRepo = scheduleTrainStationRepo;
        this.scheduleTrainRepo = scheduleTrainRepo;
        this.stationRepo = stationRepo;
        this.scheduleTrainStationHistoryRepo = scheduleTrainStationHistoryRepo;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional
    public Boolean createScheduleTrainStation(
            CreateAllScheduleTrainStation allStations) throws Exception {

        List<ScheduleTrainStationCreateReqDto> requests =
                allStations.getScheduleTrainStations();

        if (requests == null || requests.isEmpty()) {
            throw new Exception("Station list cannot be empty");
        }

        // =====================================================
        // 1. GET SCHEDULE TRAIN
        // =====================================================

        Long scheduleTrainId =
                requests.get(0).getScheduleTrainId();

        if (scheduleTrainId == null) {
            throw new Exception("Schedule train ID is required");
        }

        ScheduleTrain scheduleTrain =
                scheduleTrainRepo.findById(scheduleTrainId)
                        .orElseThrow(() ->
                                new Exception("Schedule train not found"));

        // =====================================================
        // 2. VALIDATE ALL NEW STATIONS
        // =====================================================

        // Used to detect duplicate station IDs
        Set<Long> stationIds = new HashSet<>();

        for (int i = 0; i < requests.size(); i++) {

            ScheduleTrainStationCreateReqDto reqDto =
                    requests.get(i);

            // -------------------------------------------------
            // Validate schedule train ID
            // -------------------------------------------------

            if (reqDto.getScheduleTrainId() == null) {
                throw new Exception(
                        "Schedule train ID is required");
            }

            // Every station must belong to the same schedule
            if (!reqDto.getScheduleTrainId()
                    .equals(scheduleTrainId)) {

                throw new Exception(
                        "All stations must belong to the same schedule train");
            }

            // -------------------------------------------------
            // Validate station ID
            // -------------------------------------------------

            if (reqDto.getStationId() == null) {
                throw new Exception(
                        "Station ID is required");
            }

            // -------------------------------------------------
            // Validate station exists
            // -------------------------------------------------

            stationRepo.findById(reqDto.getStationId())
                    .orElseThrow(() ->
                            new Exception(
                                    "Station not found: "
                                            + reqDto.getStationId()));

            // -------------------------------------------------
            // Rule 5:
            // Same station cannot occur twice
            // -------------------------------------------------

            if (!stationIds.add(reqDto.getStationId())) {

                throw new Exception(
                        "Station cannot occur more than once in a schedule: "
                                + reqDto.getStationId());
            }

            // -------------------------------------------------
            // Validate sequence exists
            // -------------------------------------------------

            if (reqDto.getStationSequence() == null) {
                throw new Exception(
                        "Station sequence is required");
            }

            // -------------------------------------------------
            // Rule 1 + 3 + 4:
            // sequence must be 1, 2, 3, 4...
            // -------------------------------------------------

            int expectedSequence = i + 1;

            if (reqDto.getStationSequence() != expectedSequence) {

                throw new Exception(
                        "Invalid station sequence. Expected "
                                + expectedSequence
                                + " but received "
                                + reqDto.getStationSequence());
            }

            // -------------------------------------------------
            // Validate arrival/departure time
            // -------------------------------------------------

            if (reqDto.getArrivalTime() == null) {
                throw new Exception(
                        "Arrival time is required");
            }

            if (reqDto.getDepartureTime() == null) {
                throw new Exception(
                        "Departure time is required");
            }

            if (!reqDto.getArrivalTime()
                    .before(reqDto.getDepartureTime())) {

                throw new Exception(
                        "Arrival time must be before departure time for station: "
                                + reqDto.getStationId());
            }
        }

        // =====================================================
        // 3. VALIDATE FIRST AND LAST STATION
        // =====================================================

        ScheduleTrainStationCreateReqDto firstRequest =
                requests.get(0);

        ScheduleTrainStationCreateReqDto lastRequest =
                requests.get(requests.size() - 1);

        // First station must be schedule departure station
        if (!firstRequest.getStationId()
                .equals(scheduleTrain.getDepartureStation().getId())) {

            throw new Exception(
                    "First station must be the schedule departure station");
        }

        // Last station must be schedule destination station
        if (!lastRequest.getStationId()
                .equals(scheduleTrain.getDestinationStation().getId())) {

            throw new Exception(
                    "Last station must be the schedule destination station");
        }

        // =====================================================
        // 4. GET EXISTING ACTIVE STATIONS
        // =====================================================

        List<ScheduleTrainStation> oldStations =
                scheduleTrainStationRepo
                        .findByScheduleTrain(scheduleTrain);

        // =====================================================
        // 5. MOVE OLD STATIONS TO HISTORY
        // =====================================================

        List<ScheduleTrainStationHistory> historyList =
                new ArrayList<>();

        for (ScheduleTrainStation oldStation : oldStations) {

            /*
             * Only active records should be archived.
             * If repository already returns only active records,
             * this check simply has no additional effect.
             */
            if (Boolean.TRUE.equals(oldStation.getIsDeleted())) {
                continue;
            }

            ScheduleTrainStationHistory history =
                    new ScheduleTrainStationHistory();

            history.setScheduleTrainId(
                    oldStation.getScheduleTrain().getId());

            history.setStationId(
                    oldStation.getStation().getId());

            history.setStationSequence(
                    oldStation.getStationSequence());

            history.setArrivalTime(
                    oldStation.getArrivalTime());

            history.setDepartureTime(
                    oldStation.getDepartureTime());

            history.setArchivedAt(new Date());

            historyList.add(history);

            // Soft delete old station
            oldStation.setIsDeleted(true);
        }

        if (!historyList.isEmpty()) {
            scheduleTrainStationHistoryRepo.saveAll(historyList);
        }

        // =====================================================
        // 6. DELETE OLD ACTIVE STATIONS PERMANENTLY
        // =====================================================

        scheduleTrainStationRepo
                .deleteByScheduleTrain(scheduleTrain);

        // =====================================================
        // 7. INSERT NEW ACTIVE STATIONS
        // =====================================================

        List<ScheduleTrainStation> newStations =
                new ArrayList<>();

        for (ScheduleTrainStationCreateReqDto reqDto : requests) {

            Station station =
                    stationRepo.findById(reqDto.getStationId())
                            .orElseThrow(() ->
                                    new Exception(
                                            "Station not found: "
                                                    + reqDto.getStationId()));

            ScheduleTrainStation entity =
                    new ScheduleTrainStation();

            entity.setScheduleTrain(scheduleTrain);
            entity.setStation(station);
            entity.setStationSequence(
                    reqDto.getStationSequence());
            entity.setArrivalTime(
                    reqDto.getArrivalTime());
            entity.setDepartureTime(
                    reqDto.getDepartureTime());

            newStations.add(entity);
        }

        scheduleTrainStationRepo.saveAll(newStations);

        return true;
    }

    // =====================================================
    // GET ALL
    // =====================================================

    public List<ScheduleTrainStation>
            getAllScheduleTrainStations() {

        return scheduleTrainStationRepo.findAll();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    public List<ScheduleTrainStation> searchScheduleTrainStation(
            Long id,
            Long scheduleTrainId,
            Long stationId,
            Integer stationSequence) {

        return scheduleTrainStationRepo.searchScheduleTrainStation(
                id,
                scheduleTrainId,
                stationId,
                stationSequence);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public boolean updateScheduleTrainStation(
            ScheduleTrainStationUpdateReqDto reqDto)
            throws Exception {

        ScheduleTrainStation entity =
                scheduleTrainStationRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Schedule train station not found"));

        // =====================================================
        // DETERMINE FINAL SCHEDULE TRAIN
        // =====================================================

        ScheduleTrain finalScheduleTrain =
                entity.getScheduleTrain();

        if (reqDto.getScheduleTrainId() != null) {

            finalScheduleTrain =
                    scheduleTrainRepo.findById(
                            reqDto.getScheduleTrainId())
                            .orElseThrow(() ->
                                    new Exception(
                                            "Schedule train not found"));
        }

        // =====================================================
        // DETERMINE FINAL STATION
        // =====================================================

        Station finalStation =
                entity.getStation();

        if (reqDto.getStationId() != null) {

            finalStation =
                    stationRepo.findById(
                            reqDto.getStationId())
                            .orElseThrow(() ->
                                    new Exception(
                                            "Station not found"));
        }

        // =====================================================
        // DETERMINE FINAL SEQUENCE
        // =====================================================

        Integer finalSequence =
                reqDto.getStationSequence() != null
                        ? reqDto.getStationSequence()
                        : entity.getStationSequence();

        if (finalSequence == null || finalSequence < 1) {
            throw new Exception(
                    "Station sequence must be greater than or equal to 1");
        }

        // =====================================================
        // GET ALL STATIONS OF FINAL SCHEDULE
        // =====================================================

        List<ScheduleTrainStation> scheduleStations =
                scheduleTrainStationRepo
                        .findByScheduleTrain(finalScheduleTrain);

        // =====================================================
        // VALIDATE DUPLICATE STATION
        // =====================================================

        for (ScheduleTrainStation existingStation :
                scheduleStations) {

            if (existingStation.getId()
                    .equals(entity.getId())) {
                continue;
            }

            if (Boolean.TRUE.equals(existingStation.getIsDeleted())) {
                continue;
            }

            if (existingStation.getStation().getId()
                    .equals(finalStation.getId())) {

                throw new Exception(
                        "Station cannot occur more than once in a schedule");
            }
        }

        // =====================================================
        // VALIDATE DUPLICATE SEQUENCE
        // =====================================================

        for (ScheduleTrainStation existingStation :
                scheduleStations) {

            if (existingStation.getId()
                    .equals(entity.getId())) {
                continue;
            }

            if (Boolean.TRUE.equals(existingStation.getIsDeleted())) {
                continue;
            }

            if (existingStation.getStationSequence()
                    .equals(finalSequence)) {

                throw new Exception(
                        "Station sequence "
                                + finalSequence
                                + " is already used in this schedule");
            }
        }

        // =====================================================
        // UPDATE ENTITY
        // =====================================================

        if (reqDto.getScheduleTrainId() != null) {
            entity.setScheduleTrain(finalScheduleTrain);
        }

        if (reqDto.getStationId() != null) {
            entity.setStation(finalStation);
        }

        /*
         * Important:
         * use != null instead of != 0.
         * Integer can be null when the field is not provided.
         */
        if (reqDto.getStationSequence() != null) {
            entity.setStationSequence(
                    reqDto.getStationSequence());
        }

        if (reqDto.getArrivalTime() != null) {
            entity.setArrivalTime(
                    reqDto.getArrivalTime());
        }

        if (reqDto.getDepartureTime() != null) {
            entity.setDepartureTime(
                    reqDto.getDepartureTime());
        }

        // =====================================================
        // VALIDATE FINAL ARRIVAL / DEPARTURE TIME
        // =====================================================

        Date finalArrivalTime =
                reqDto.getArrivalTime() != null
                        ? reqDto.getArrivalTime()
                        : entity.getArrivalTime();

        Date finalDepartureTime =
                reqDto.getDepartureTime() != null
                        ? reqDto.getDepartureTime()
                        : entity.getDepartureTime();

        if (finalArrivalTime == null) {
            throw new Exception(
                    "Arrival time is required");
        }

        if (finalDepartureTime == null) {
            throw new Exception(
                    "Departure time is required");
        }

        if (!finalArrivalTime.before(finalDepartureTime)) {

            throw new Exception(
                    "Arrival time must be before departure time");
        }

        // =====================================================
        // SAVE
        // =====================================================

        scheduleTrainStationRepo.save(entity);

        return true;
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Transactional
    public boolean deleteScheduleTrainStation(
            ScheduleTrainStationDeleteReqDto reqDto)
            throws Exception {

        ScheduleTrainStation entity =
                scheduleTrainStationRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Schedule train station not found"));

        entity.setIsDeleted(true);

        scheduleTrainStationRepo.save(entity);

        return true;
    }

    // =====================================================
    // PERMANENT DELETE
    // =====================================================

    @Transactional
    public boolean deleteScheduleTrainStationPermanently(
            ScheduleTrainStationDeleteReqDto reqDto)
            throws Exception {

        ScheduleTrainStation entity =
                scheduleTrainStationRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Schedule train station not found"));

        scheduleTrainStationRepo.delete(entity);

        return true;
    }

    // =====================================================
    // HISTORY
    // =====================================================

    // GET ALL
    public List<ScheduleTrainStationHistory>
            getAllScheduleTrainStationsHistory() {

        return scheduleTrainStationHistoryRepo.findAll();
    }

    // SEARCH
    public List<ScheduleTrainStationHistory>
            searchScheduleTrainStationHistorys(
                    Long id,
                    Long scheduleTrainId,
                    Long stationId,
                    Integer stationSequence) {

        return scheduleTrainStationHistoryRepo
                .searchScheduleTrainStationHistorys(
                        id,
                        scheduleTrainId,
                        stationId,
                        stationSequence);
    }




    @Transactional
    public void archive(
            ScheduleTrainStation station) {

        ScheduleTrainStationHistory history =
                new ScheduleTrainStationHistory();

        history.setScheduleTrainId(
                station.getScheduleTrain()
                        .getId()
        );

        history.setStationId(
                station.getStation()
                        .getId()
        );

        history.setStationSequence(
                station.getStationSequence()
        );

        history.setArrivalTime(
                station.getArrivalTime()
        );

        history.setDepartureTime(
                station.getDepartureTime()
        );

        history.setArchivedAt(
                new Date()
        );

        scheduleTrainStationHistoryRepo.save(
                history
        );
    }





    
}

