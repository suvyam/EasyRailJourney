package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.Trains.TrainStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Station;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.StationRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainRepo;

import jakarta.transaction.Transactional;







@Service
public class ScheduleTrainService {

    private final ScheduleTrainRepo scheduleRepository;
    private final TrainRepo trainRepo;
    private final StationRepo stationRepo;
    private final ScheduleTrainHistoryService scheduleTrainHistoryService;

    public ScheduleTrainService(
            ScheduleTrainRepo scheduleRepository,
            TrainRepo trainRepo,
            StationRepo stationRepo,
            ScheduleTrainHistoryService scheduleTrainHistoryService) {

        this.scheduleRepository = scheduleRepository;
        this.trainRepo = trainRepo;
        this.stationRepo = stationRepo;
        this.scheduleTrainHistoryService = scheduleTrainHistoryService;
    }

    // =====================================================
    // CREATE
    // =====================================================

    public ScheduleRespDto createSchedule(
            ScheduleCreateReqDto reqDto) throws Exception {

        // Here you will find Train and Stations using their IDs

        Train train = trainRepo.findById(reqDto.getTrainId())
                .orElseThrow(() -> new Exception("Train not found"));

        if (scheduleRepository
                .searchSchedule(train.getId(), null, null, null, false)
                .size() > 0) {

            throw new Exception("train already scheduled");
        }

        Station departureStation =
                stationRepo.findById(reqDto.getDepartureStationId())
                        .orElseThrow(() ->
                                new Exception("Departure station not found"));

        Station destinationStation =
                stationRepo.findById(reqDto.getDestinationStationId())
                        .orElseThrow(() ->
                                new Exception("Destination station not found"));

        // Check journey time

        if (!reqDto.getJourneyStartTime()
                .before(reqDto.getJourneyEstimatedEndTime())) {

            throw new Exception(
                    "Journey start time must be before journey estimated end time"
            );
        }

        // Check overlapping schedule for same train

        List<ScheduleTrain> overlappingSchedules =
                scheduleRepository.findOverlappingSchedules(
                        train.getId(),
                        reqDto.getJourneyStartTime(),
                        reqDto.getJourneyEstimatedEndTime()
                );

        if (!overlappingSchedules.isEmpty()) {

            throw new Exception(
                    "Train already has an overlapping schedule for this time"
            );
        }

        ScheduleTrain schedule = new ScheduleTrain();

        schedule.setTrain(train);
        schedule.setDepartureStation(departureStation);
        schedule.setDestinationStation(destinationStation);
        schedule.setJourneyStartTime(reqDto.getJourneyStartTime());
        schedule.setJourneyEstimatedEndTime(
                reqDto.getJourneyEstimatedEndTime());
        schedule.setStatus(reqDto.getStatus());

        scheduleRepository.save(schedule);

        List<ScheduleTrain> scheduleTrains = new ArrayList<>();
        scheduleTrains.add(schedule);

        return wrapper(scheduleTrains).get(0);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    public List<ScheduleRespDto> getAllSchedules() {

        List<ScheduleTrain> ans =
                scheduleRepository.findAll();

        return wrapper(ans);
    }

    // =====================================================
    // SEARCH
    // =====================================================

    public List<ScheduleRespDto> searchSchedule(
            Long id,
            Long trainId,
            Long departureStationId,
            Long destinationStationId,
            TrainStatus status,
            boolean isDeleted) {

        List<ScheduleTrain> list =
                scheduleRepository.searchSchedule(
                        id,
                        trainId,
                        departureStationId,
                        destinationStationId,
                        isDeleted
                );

        return wrapper(list);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public boolean updateSchedule(
            ScheduleUpdateReqDto reqDto) throws Exception {

        Train train = null;

        if (reqDto.getTrainId() != null) {

            train = trainRepo.findById(reqDto.getTrainId())
                    .orElseThrow(() ->
                            new Exception("Train not found"));
        }

        Station departureStation = null;
        Station destinationStation = null;

        if (reqDto.getDepartureStationId() != null) {

            departureStation = stationRepo.findById(
                    reqDto.getDepartureStationId()
            ).orElseThrow(() ->
                    new Exception("Departure station not found"));
        }

        if (reqDto.getDestinationStationId() != null) {

            destinationStation = stationRepo.findById(
                    reqDto.getDestinationStationId()
            ).orElseThrow(() ->
                    new Exception("Destination station not found"));
        }

        Optional<ScheduleTrain> scheduleOptional =
                scheduleRepository.findById(reqDto.getId());

        if (scheduleOptional.isEmpty())
            throw new Exception("Schedule not found to update");

        ScheduleTrain schedule = scheduleOptional.get();

        // =====================================================
        // ARCHIVE OLD STATE BEFORE UPDATE
        // =====================================================

        scheduleTrainHistoryService.archive(schedule);

        // =====================================================
        // CHECK FINAL JOURNEY TIME
        // =====================================================

        Date finalStartTime =
                reqDto.getJourneyStartTime() != null
                        ? reqDto.getJourneyStartTime()
                        : schedule.getJourneyStartTime();

        Date finalEndTime =
                reqDto.getJourneyEstimatedEndTime() != null
                        ? reqDto.getJourneyEstimatedEndTime()
                        : schedule.getJourneyEstimatedEndTime();

        if (!finalStartTime.before(finalEndTime)) {

            throw new Exception(
                    "Journey start time must be before journey estimated end time"
            );
        }

        // =====================================================
        // CHECK OVERLAPPING SCHEDULE FOR SAME TRAIN
        // =====================================================

        Long finalTrainId =
                reqDto.getTrainId() != null
                        ? reqDto.getTrainId()
                        : schedule.getTrain().getId();

        List<ScheduleTrain> overlappingSchedules =
                scheduleRepository.findOverlappingSchedules(
                        finalTrainId,
                        finalStartTime,
                        finalEndTime
                );

        for (ScheduleTrain existingSchedule : overlappingSchedules) {

            if (!existingSchedule.getId().equals(schedule.getId())) {

                throw new Exception(
                        "Train already has an overlapping schedule for this time"
                );
            }
        }

        if (train != null)
            schedule.setTrain(train);

        if (departureStation != null)
            schedule.setDepartureStation(departureStation);

        if (destinationStation != null)
            schedule.setDestinationStation(destinationStation);

        if (reqDto.getJourneyStartTime() != null)
            schedule.setJourneyStartTime(
                    reqDto.getJourneyStartTime());

        if (reqDto.getJourneyEstimatedEndTime() != null)
            schedule.setJourneyEstimatedEndTime(
                    reqDto.getJourneyEstimatedEndTime());

        if (reqDto.getStatus() != null)
            schedule.setStatus(reqDto.getStatus());

        scheduleRepository.save(schedule);

        return true;
    }

    // =====================================================
    // PERMANENT DELETE
    // =====================================================

    @Transactional
    public boolean deleteSchedulePermanently(
            ScheduleDeleteReqDto reqDto) throws Exception {

        Optional<ScheduleTrain> scheduleOptional =
                scheduleRepository.findById(reqDto.getId());

        if (scheduleOptional.isEmpty())
            throw new Exception("Schedule not found");

        ScheduleTrain schedule =
                scheduleOptional.get();

        // =====================================================
        // ARCHIVE BEFORE DELETE
        // =====================================================

        scheduleTrainHistoryService.archive(schedule);

        scheduleRepository.delete(schedule);

        return true;
    }

    // =====================================================
    // WRAPPER
    // =====================================================

    public List<ScheduleRespDto> wrapper(
            List<ScheduleTrain> req) {

        List<ScheduleRespDto> list =
                new ArrayList<>();

        for (ScheduleTrain st : req) {

            ScheduleRespDto respDto =
                    new ScheduleRespDto();

            respDto.setId(st.getId());
            respDto.setTrainId(st.getTrain().getId());
            respDto.setDepartureStationName(
                    st.getDepartureStation().getName());
            respDto.setDestinationStationName(
                    st.getDestinationStation().getName());
            respDto.setJourneyStartTime(
                    st.getJourneyStartTime());
            respDto.setJourneyEstimatedEndTime(
                    st.getJourneyEstimatedEndTime());
            respDto.setStatus(st.getStatus());

            list.add(respDto);
        }

        return list;
    }
}