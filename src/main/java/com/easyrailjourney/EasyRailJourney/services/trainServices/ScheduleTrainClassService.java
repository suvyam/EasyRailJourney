package com.easyrailjourney.EasyRailJourney.services.trainServices;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses.ScheduleTrainClassCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses.ScheduleTrainClassDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses.ScheduleTrainClassRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses.ScheduleTrainClassUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Coach;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClass;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainCoach;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Seat;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.SeatType;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainClass;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainClassRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainClassSeatRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainClassRepo;

import jakarta.transaction.Transactional;

@Service
public class ScheduleTrainClassService {

    private final ScheduleTrainClassRepo scheduleTrainClassRepo;
    private final ScheduleTrainRepo scheduleTrainRepo;
    private final TrainClassRepo trainClassRepo;
    private final ScheduleTrainClassSeatRepo scheduleTrainClassSeatRepo;
    private final ScheduleTrainClassHistoryService
            scheduleTrainClassHistoryService;

    public ScheduleTrainClassService(
            ScheduleTrainClassRepo scheduleTrainClassRepo,
            ScheduleTrainRepo scheduleTrainRepo,
            TrainClassRepo trainClassRepo,
            ScheduleTrainClassSeatRepo scheduleTrainClassSeatRepo,
            ScheduleTrainClassHistoryService
                    scheduleTrainClassHistoryService) {

        this.scheduleTrainClassRepo = scheduleTrainClassRepo;
        this.scheduleTrainRepo = scheduleTrainRepo;
        this.trainClassRepo = trainClassRepo;
        this.scheduleTrainClassSeatRepo = scheduleTrainClassSeatRepo;
        this.scheduleTrainClassHistoryService =
                scheduleTrainClassHistoryService;
    }

    // CREATE
    @Transactional 
    public ScheduleTrainClassRespDto createScheduleTrainClass(
            ScheduleTrainClassCreateReqDto reqDto) throws Exception {

        ScheduleTrain scheduleTrain =
                scheduleTrainRepo.findById(reqDto.getScheduleTrainId())
                        .orElseThrow(() ->
                                new Exception("Schedule train not found"));

        TrainClass trainClass =
                trainClassRepo.findById(reqDto.getTrainClassId())
                        .orElseThrow(() ->
                                new Exception("Train class not found"));

        if (scheduleTrainClassRepo
                .findByScheduleTrainAndTrainClass(
                        scheduleTrain,
                        trainClass)
                .isPresent()) {

            throw new Exception(
                    "Train class already assigned to this schedule");
        }

        ScheduleTrainClass entity =
                new ScheduleTrainClass();

        entity.setScheduleTrain(scheduleTrain);
        entity.setTrainClass(trainClass);

        scheduleTrainClassRepo.save(entity);

        List<ScheduleTrainClass> list =
                new ArrayList<>();

        list.add(entity);

        return wrapper(list).get(0);
    }

    // GET ALL
    public List<ScheduleTrainClassRespDto> getAllScheduleTrainClasses() {

        List<ScheduleTrainClass> list =
                scheduleTrainClassRepo.findAll();

        return wrapper(list);
    }

    // SEARCH
    public List<ScheduleTrainClassRespDto> searchScheduleTrainClass(
            Long id,
            Long scheduleTrainId,
            Long trainClassId) {

        List<ScheduleTrainClass> list =
                scheduleTrainClassRepo.searchScheduleTrainClass(
                        id,
                        scheduleTrainId,
                        trainClassId
                );

        return wrapper(list);
    }

    // UPDATE
    @Transactional
    public boolean updateScheduleTrainClass(
            ScheduleTrainClassUpdateReqDto reqDto) throws Exception {

        ScheduleTrainClass entity =
                scheduleTrainClassRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Schedule train class not found"));

        // =====================================================
        // ARCHIVE OLD STATE BEFORE UPDATE
        // =====================================================

        scheduleTrainClassHistoryService.archive(entity);

        if (reqDto.getScheduleTrainId() != null) {

            ScheduleTrain scheduleTrain =
                    scheduleTrainRepo.findById(
                            reqDto.getScheduleTrainId())
                    .orElseThrow(() ->
                            new Exception(
                                    "Schedule train not found"));

            entity.setScheduleTrain(scheduleTrain);
        }

        if (reqDto.getTrainClassId() != null) {

            TrainClass trainClass =
                    trainClassRepo.findById(
                            reqDto.getTrainClassId())
                    .orElseThrow(() ->
                            new Exception(
                                    "Train class not found"));

            entity.setTrainClass(trainClass);
        }

        scheduleTrainClassRepo.save(entity);

        return true;
    }

    // SOFT DELETE
    @Transactional
    public boolean deleteScheduleTrainClass(
            ScheduleTrainClassDeleteReqDto reqDto)
            throws Exception {

        ScheduleTrainClass entity =
                scheduleTrainClassRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Schedule train class not found"));

        // =====================================================
        // ARCHIVE OLD STATE BEFORE SOFT DELETE
        // =====================================================

        scheduleTrainClassHistoryService.archive(entity);

        entity.setIsDeleted(true);

        scheduleTrainClassRepo.save(entity);

        return true;
    }

    // PERMANENT DELETE
    @Transactional
    public boolean deleteScheduleTrainClassPermanently(
            ScheduleTrainClassDeleteReqDto reqDto)
            throws Exception {

        ScheduleTrainClass entity =
                scheduleTrainClassRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Schedule train class not found"));

        // =====================================================
        // ARCHIVE OLD STATE BEFORE PERMANENT DELETE
        // =====================================================

        scheduleTrainClassHistoryService.archive(entity);

        scheduleTrainClassRepo.delete(entity);

        return true;
    }

    // SCHEDULE TRAIN CLASS SEAT GET
    public List<ScheduleTrainClassSeat> getSeatsByScheduleTrainClass(
            Long scheduleTrainClassId) throws Exception {

        ScheduleTrainClass scheduleTrainClass =
                scheduleTrainClassRepo.findById(scheduleTrainClassId)
                        .orElseThrow(() ->
                                new Exception(
                                        "Schedule train class not found"));

        return scheduleTrainClassSeatRepo
                .findByScheduleTrainClass_IdAndIsDeletedFalse(
                        scheduleTrainClass.getId()
                );
    }

    public List<ScheduleTrainClassRespDto> wrapper(
            List<ScheduleTrainClass> list) {

        List<ScheduleTrainClassRespDto> resp =
                new ArrayList<>();

        for (ScheduleTrainClass entity : list) {

            ScheduleTrainClassRespDto scheduleTrainClassRespDto =
                    new ScheduleTrainClassRespDto();

            scheduleTrainClassRespDto.setScheduleTrainName(
                    entity.getScheduleTrain()
                            .getTrain()
                            .getTrainName()
            );

            scheduleTrainClassRespDto.setScheduleTrainNumber(
                    entity.getScheduleTrain()
                            .getTrain()
                            .getTrainNumber()
            );

            scheduleTrainClassRespDto.setClassName(
                    entity.getTrainClass()
                            .getClassName()
            );

            Set<String> coachNames = new HashSet<>();

            for (ScheduleTrainCoach scheduleTrainCoach :
                    entity.getCoaches()) {

                Coach coach =
                        scheduleTrainCoach.getCoach();

                if (coach != null) {
                    coachNames.add(
                            coach.getCoachNumber()
                    );
                }
            }

            scheduleTrainClassRespDto.setCoacheNames(
                    coachNames
            );

            Map<SeatType, List<String>> scheduleTrainClassSeats =
                    new HashMap<>();

            for (ScheduleTrainCoach scheduleTrainCoach :
                    entity.getCoaches()) {

                Coach coach =
                        scheduleTrainCoach.getCoach();

                if (coach == null ||
                        coach.getSeats() == null) {
                    continue;
                }

                for (Seat seat : coach.getSeats()) {

                    if (seat.getSeatType() == null ||
                            seat.getSeatNumber() == null) {
                        continue;
                    }

                    scheduleTrainClassSeats
                            .computeIfAbsent(
                                    seat.getSeatType(),
                                    k -> new ArrayList<>()
                            )
                            .add(seat.getSeatNumber());
                }
            }

            scheduleTrainClassRespDto.setIsDeleted(
                    entity.getIsDeleted()
            );

            resp.add(scheduleTrainClassRespDto);
        }

        return resp;
    }
}