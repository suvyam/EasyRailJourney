package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

// No duplicate sequence number
// Sequence starts from 1
// Sequence increases by exactly 1
// No gaps
// Each station should occur only once in a schedule,

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

    // CREATE
 
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

    ScheduleTrain scheduleTrain =
            scheduleTrainRepo.findById(scheduleTrainId)
                    .orElseThrow(() ->
                            new Exception("Schedule train not found"));

    // =====================================================
    // 2. VALIDATE ALL NEW STATIONS FIRST
    // =====================================================

    for (ScheduleTrainStationCreateReqDto reqDto : requests) {

        // if (!reqDto.getStationSequence()) {
        //     // add your required validation here
        // }

        stationRepo.findById(reqDto.getStationId())
                .orElseThrow(() ->
                        new Exception(
                                "Station not found: "
                                        + reqDto.getStationId()));
    }

    // =====================================================
    // 3. GET EXISTING ACTIVE STATIONS
    // =====================================================

    List<ScheduleTrainStation> oldStations =
            scheduleTrainStationRepo
                    .findByScheduleTrain(scheduleTrain);

    // =====================================================
    // 4. MOVE OLD STATIONS TO HISTORY
    // =====================================================

    List<ScheduleTrainStationHistory> historyList =
            new ArrayList<>();

    for (ScheduleTrainStation oldStation : oldStations) {

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

    scheduleTrainStationHistoryRepo.saveAll(historyList);

        // =====================================================
        // 5. DELETE OLD ACTIVE STATIONS PERMANENTLY
        // =====================================================

        scheduleTrainStationRepo
        .deleteByScheduleTrain(scheduleTrain);

    // =====================================================
    // 6. INSERT NEW ACTIVE STATIONS
    // =====================================================

    List<ScheduleTrainStation> newStations =
            new ArrayList<>();

    for (ScheduleTrainStationCreateReqDto reqDto : requests) {

        Station station =
                stationRepo.findById(reqDto.getStationId())
                        .orElseThrow(() ->
                                new Exception("Station not found"));

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

    // GET ALL
    public List<ScheduleTrainStation>
            getAllScheduleTrainStations() {

        return scheduleTrainStationRepo.findAll();
    }



    // SEARCH
    public List<ScheduleTrainStation> searchScheduleTrainStation(
            Long id,
            Long scheduleTrainId,
            Long stationId,
            Integer stationSequence) {

        return scheduleTrainStationRepo.searchScheduleTrainStation(
                id,
                scheduleTrainId,
                stationId,
                stationSequence
        );
    }

    // UPDATE
    public boolean updateScheduleTrainStation(
            ScheduleTrainStationUpdateReqDto reqDto)
            throws Exception {

        ScheduleTrainStation entity =
                scheduleTrainStationRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Schedule train station not found"));

        if (reqDto.getScheduleTrainId() != null) {

            ScheduleTrain scheduleTrain =
                    scheduleTrainRepo.findById(
                            reqDto.getScheduleTrainId())
                    .orElseThrow(() ->
                            new Exception(
                                    "Schedule train not found"));

            entity.setScheduleTrain(scheduleTrain);
        }

        if (reqDto.getStationId() != null) {

            Station station =
                    stationRepo.findById(
                            reqDto.getStationId())
                    .orElseThrow(() ->
                            new Exception("Station not found"));

            entity.setStation(station);
        }

        if (reqDto.getStationSequence() != 0) {
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

        scheduleTrainStationRepo.save(entity);

        return true;
    }

    // SOFT DELETE
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

    // PERMANENT DELETE
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




    // HISTORY

     // GET ALL
     public List<ScheduleTrainStationHistory>
     getAllScheduleTrainStationsHistory() {

        return scheduleTrainStationHistoryRepo.findAll();
        }



        // SEARCH
        public List<ScheduleTrainStationHistory> searchScheduleTrainStationHistorys(
        Long id,
        Long scheduleTrainId,
        Long stationId,
        Integer stationSequence) {

        return scheduleTrainStationHistoryRepo.searchScheduleTrainStationHistorys(
                id,
                scheduleTrainId,
                stationId,
                stationSequence
        );
        }


}