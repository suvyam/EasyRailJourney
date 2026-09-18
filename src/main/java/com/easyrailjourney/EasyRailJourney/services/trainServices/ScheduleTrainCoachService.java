package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCoach.ScheduleTrainCoachCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCoach.ScheduleTrainCoachDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleCoach.ScheduleTrainCoachUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.GeneralStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Coach;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClass;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainCoach;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Seat;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.CoachRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainClassRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainClassSeatRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainCoachRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainRepo;

import jakarta.transaction.Transactional;

@Service
public class ScheduleTrainCoachService {

    private final ScheduleTrainCoachRepo scheduleTrainCoachRepo;
    private final ScheduleTrainClassRepo scheduleTrainClassRepo;
    private final CoachRepo coachRepo;
    private final ScheduleTrainClassSeatRepo scheduleTrainClassSeatRepo;
    private final ScheduleTrainRepo scheduleTrainRepo;

    private final ScheduleTrainCoachHistoryService
            scheduleTrainCoachHistoryService;

    private final ScheduleTrainClassSeatHistoryService
            scheduleTrainClassSeatHistoryService;

    public ScheduleTrainCoachService(
            ScheduleTrainCoachRepo scheduleTrainCoachRepo,
            ScheduleTrainClassRepo scheduleTrainClassRepo,
            CoachRepo coachRepo,
            ScheduleTrainClassSeatRepo scheduleTrainClassSeatRepo,
            ScheduleTrainRepo scheduleTrainRepo,
            ScheduleTrainCoachHistoryService
                    scheduleTrainCoachHistoryService,
            ScheduleTrainClassSeatHistoryService
                    scheduleTrainClassSeatHistoryService) {

        this.scheduleTrainCoachRepo = scheduleTrainCoachRepo;
        this.scheduleTrainClassRepo = scheduleTrainClassRepo;
        this.coachRepo = coachRepo;
        this.scheduleTrainClassSeatRepo = scheduleTrainClassSeatRepo;
        this.scheduleTrainRepo = scheduleTrainRepo;

        this.scheduleTrainCoachHistoryService =
                scheduleTrainCoachHistoryService;

        this.scheduleTrainClassSeatHistoryService =
                scheduleTrainClassSeatHistoryService;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional
    public ScheduleTrainCoach createScheduleTrainCoach(
            ScheduleTrainCoachCreateReqDto reqDto)
            throws Exception {

        ScheduleTrainClass scheduleTrainClass =
                scheduleTrainClassRepo.findById(
                        reqDto.getScheduleTrainClassId())
                .orElseThrow(() ->
                        new Exception(
                                "Schedule train class not found"));

        Coach coach =
                coachRepo.findById(reqDto.getCoachId())
                .orElseThrow(() ->
                        new Exception("Coach not found"));

        // Check same coach already assigned
        // to the same schedule train class
        if (scheduleTrainCoachRepo
                .findByScheduleTrainClassAndCoach(
                        scheduleTrainClass,
                        coach)
                .isPresent()) {

            throw new Exception(
                    "Coach already assigned to this schedule class");
        }

        // GET CURRENT SCHEDULE TRAIN
        ScheduleTrain scheduleTrain =
                scheduleTrainClass.getScheduleTrain();

        if (scheduleTrain == null) {
            throw new Exception(
                    "Schedule train not found for this schedule class");
        }

        // GET CURRENT JOURNEY TIME
        Date journeyStartTime =
                scheduleTrain.getJourneyStartTime();

        Date journeyEndTime =
                scheduleTrain.getJourneyEstimatedEndTime();

        // CHECK ALL OVERLAPPING SCHEDULES
        List<ScheduleTrain> overlappingSchedules =
                scheduleTrainRepo.findOverlappingSchedulesByCoach(
                        coach.getId(),
                        journeyStartTime,
                        journeyEndTime
                );

        if (!overlappingSchedules.isEmpty()) {
            throw new Exception(
                    "Coach is already assigned to another schedule train during this time"
            );
        }

        // CHECK WHETHER SAME COACH EXISTS
        // IN ANY OVERLAPPING SCHEDULE
        List<ScheduleTrainCoach> conflictingCoaches =
                scheduleTrainCoachRepo
                        .findByScheduleTrainClass_ScheduleTrainInAndCoachAndIsDeletedFalse(
                                overlappingSchedules,
                                coach
                        );

        if (!conflictingCoaches.isEmpty()) {
            throw new Exception(
                    "Coach is already assigned to another schedule train during this time"
            );
        }

        ScheduleTrainCoach entity =
                new ScheduleTrainCoach();

        entity.setScheduleTrainClass(scheduleTrainClass);
        entity.setCoach(coach);
        entity.setCoachPosition(reqDto.getCoachPosition());
        entity.setStatus(reqDto.getStatus());

        scheduleTrainCoachRepo.save(entity);

        // CREATE SEAT RECORDS FOR NEW ASSIGNMENT

        System.out.println("hh");
        List<ScheduleTrainClassSeat> seats =
                entity.getCoach().getSeats()
                .stream()
                .map(seat -> {

                    ScheduleTrainClassSeat stccs =
                            new ScheduleTrainClassSeat();

                    stccs.setScheduleTrainClass(
                            scheduleTrainClass);

                    stccs.setSeat(seat);

                    stccs.setWaitListCount(0);

                    stccs.setSeatStatus(
                            SeatStatus.UNLOCKED);

                    stccs.setScheduleTrainCoach(
                            entity);

                    return stccs;
                })
                .collect(Collectors.toList());

        scheduleTrainClassSeatRepo.saveAll(seats);

        return entity;
    }

    // =====================================================
    // GET ALL
    // =====================================================

    public List<ScheduleTrainCoach> getAllScheduleTrainCoaches() {

        return scheduleTrainCoachRepo.findAll();
    }

    // =====================================================
    // SEARCH
    // =====================================================

    public List<ScheduleTrainCoach> searchScheduleTrainCoach(
            Long id,
            Long scheduleTrainClassId,
            Long coachId,
            String coachPosition,
            GeneralStatus status) {

        return scheduleTrainCoachRepo.searchScheduleTrainCoach(
                id,
                scheduleTrainClassId,
                coachId,
                coachPosition,
                status
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Transactional
    public boolean updateScheduleTrainCoach(
            ScheduleTrainCoachUpdateReqDto reqDto)
            throws Exception {

        ScheduleTrainCoach entity =
                scheduleTrainCoachRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Schedule train coach not found"));

        // =====================================================
        // OLD VALUES
        // =====================================================

        ScheduleTrainClass oldScheduleTrainClass =
                entity.getScheduleTrainClass();

        Coach oldCoach =
                entity.getCoach();

        ScheduleTrainClass scheduleTrainClass =
                oldScheduleTrainClass;

        Coach coach =
                oldCoach;

        // =====================================================
        // FIND NEW SCHEDULE TRAIN CLASS
        // =====================================================

        if (reqDto.getScheduleTrainClassId() != null) {

            scheduleTrainClass =
                    scheduleTrainClassRepo.findById(
                            reqDto.getScheduleTrainClassId())
                    .orElseThrow(() ->
                            new Exception(
                                    "Schedule train class not found"));
        }

        // =====================================================
        // FIND NEW COACH
        // =====================================================

        if (reqDto.getCoachId() != null) {

            coach =
                    coachRepo.findById(
                            reqDto.getCoachId())
                    .orElseThrow(() ->
                            new Exception(
                                    "Coach not found"));
        }

        // =====================================================
        // CHECK SAME COACH + SAME SCHEDULE CLASS
        // =====================================================

        if (!scheduleTrainClass
                .equals(entity.getScheduleTrainClass())
                || !coach.equals(entity.getCoach())) {

            Optional<ScheduleTrainCoach> existingAssignment =
                    scheduleTrainCoachRepo
                            .findByScheduleTrainClassAndCoach(
                                    scheduleTrainClass,
                                    coach
                            );

            if (existingAssignment.isPresent()
                    && !existingAssignment.get()
                            .getId()
                            .equals(entity.getId())) {

                throw new Exception(
                        "Coach already assigned to this schedule class");
            }
        }

        // =====================================================
        // GET SCHEDULE TRAIN OF NEW SCHEDULE CLASS
        // =====================================================

        ScheduleTrain scheduleTrain =
                scheduleTrainClass.getScheduleTrain();

        if (scheduleTrain == null) {
            throw new Exception(
                    "Schedule train not found for this schedule class");
        }

        // =====================================================
        // GET JOURNEY TIME
        // =====================================================

        Date journeyStartTime =
                scheduleTrain.getJourneyStartTime();

        Date journeyEndTime =
                scheduleTrain.getJourneyEstimatedEndTime();

        // =====================================================
        // FIND OVERLAPPING SCHEDULES
        // =====================================================

        List<ScheduleTrain> overlappingSchedules =
                scheduleTrainRepo.findOverlappingSchedulesByCoach(
                        coach.getId(),
                        journeyStartTime,
                        journeyEndTime
                );

        // Remove current schedule from overlap result if present
        overlappingSchedules.removeIf(
                existingSchedule ->
                        existingSchedule.getId()
                                .equals(scheduleTrain.getId())
        );

        if (!overlappingSchedules.isEmpty()) {
            throw new Exception(
                    "Coach is already assigned to another schedule train during this time"
            );
        }

        // =====================================================
        // FIND SAME COACH IN OVERLAPPING SCHEDULES
        // =====================================================

        List<ScheduleTrainCoach> conflictingCoaches =
                scheduleTrainCoachRepo
                        .findByScheduleTrainClass_ScheduleTrainInAndCoachAndIsDeletedFalse(
                                overlappingSchedules,
                                coach
                        );

        if (!conflictingCoaches.isEmpty()) {
            throw new Exception(
                    "Coach is already assigned to another schedule train during this time"
            );
        }

        // =====================================================
        // CHECK WHETHER ASSIGNMENT CHANGED
        // =====================================================

        boolean coachChanged =
                !oldCoach.getId()
                        .equals(coach.getId());

        boolean scheduleTrainClassChanged =
                !oldScheduleTrainClass.getId()
                        .equals(scheduleTrainClass.getId());

        boolean assignmentChanged =
                coachChanged || scheduleTrainClassChanged;

        // =====================================================
        // ARCHIVE OLD COACH STATE
        // =====================================================

        scheduleTrainCoachHistoryService.archive(entity);

        // =====================================================
        // IF ASSIGNMENT CHANGED
        // PRESERVE EXISTING SEAT RECORDS
        // =====================================================

        if (assignmentChanged) {

            List<ScheduleTrainClassSeat> oldSeats =
                    scheduleTrainClassSeatRepo
                            .findByScheduleTrainCoach(entity);

            // -------------------------------------------------
            // COACH REPLACEMENT VALIDATION
            // -------------------------------------------------

            if (coachChanged) {

                if (!oldCoach.getCoachType().getId()
                        .equals(coach.getCoachType().getId())) {

                    throw new Exception(
                            "Replacement coach must have the same coach type");
                }

                if (oldCoach.getSeats().size()
                        != coach.getSeats().size()) {

                    throw new Exception(
                            "Replacement coach must have the same number of seats");
                }
            }

            // -------------------------------------------------
            // ARCHIVE OLD SEAT STATE
            // -------------------------------------------------

            if (!oldSeats.isEmpty()) {

                scheduleTrainClassSeatHistoryService
                        .archiveAll(oldSeats);
            }

            // -------------------------------------------------
            // BUILD NEW COACH SEAT MAP
            // -------------------------------------------------

            Map<String, Seat> newCoachSeats =
                    coach.getSeats()
                            .stream()
                            .collect(Collectors.toMap(
                                    Seat::getSeatNumber,
                                    seat -> seat
                            ));

            // -------------------------------------------------
            // UPDATE EXISTING SEAT RECORDS
            // -------------------------------------------------

            for (ScheduleTrainClassSeat oldSeat : oldSeats) {

                Seat newSeat =
                        newCoachSeats.get(
                                oldSeat
                                        .getSeat()
                                        .getSeatNumber());

                if (newSeat == null) {

                    throw new Exception(
                            "Replacement coach does not contain seat "
                                    + oldSeat.getSeat()
                                        .getSeatNumber());
                }

                // Only replace references.
                // Booking/state information stays untouched.

                oldSeat.setScheduleTrainCoach(
                        entity);

                oldSeat.setScheduleTrainClass(
                        scheduleTrainClass);

                oldSeat.setSeat(newSeat);
            }

            scheduleTrainClassSeatRepo.saveAll(oldSeats);
        }

        // =====================================================
        // APPLY COACH CHANGES
        // =====================================================

        if (reqDto.getScheduleTrainClassId() != null) {
            entity.setScheduleTrainClass(
                    scheduleTrainClass);
        }

        if (reqDto.getCoachId() != null) {
            entity.setCoach(coach);
        }

        if (reqDto.getCoachPosition() != null) {
            entity.setCoachPosition(
                    reqDto.getCoachPosition());
        }

        if (reqDto.getStatus() != null) {
            entity.setStatus(
                    reqDto.getStatus());
        }

        scheduleTrainCoachRepo.save(entity);

        return true;
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Transactional
    public boolean deleteScheduleTrainCoach(
            ScheduleTrainCoachDeleteReqDto reqDto)
            throws Exception {

        ScheduleTrainCoach entity =
                scheduleTrainCoachRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Schedule train coach not found"));

        // Archive coach
        scheduleTrainCoachHistoryService.archive(entity);

        // Archive current seat states
        List<ScheduleTrainClassSeat> seats =
                scheduleTrainClassSeatRepo
                        .findByScheduleTrainCoach(entity);

        if (!seats.isEmpty()) {

            scheduleTrainClassSeatHistoryService
                    .archiveAll(seats);
        }

        entity.setIsDeleted(true);

        scheduleTrainCoachRepo.save(entity);

        return true;
    }

    // =====================================================
    // PERMANENT DELETE
    // =====================================================

    @Transactional
    public boolean deleteScheduleTrainCoachPermanently(
            ScheduleTrainCoachDeleteReqDto reqDto)
            throws Exception {

        ScheduleTrainCoach entity =
                scheduleTrainCoachRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception(
                                        "Schedule train coach not found"));

        // Archive coach
        scheduleTrainCoachHistoryService.archive(entity);

        // Archive seats before deletion
        List<ScheduleTrainClassSeat> seats =
                scheduleTrainClassSeatRepo
                        .findByScheduleTrainCoach(entity);

        if (!seats.isEmpty()) {

            scheduleTrainClassSeatHistoryService
                    .archiveAll(seats);

            scheduleTrainClassSeatRepo
                    .deleteAll(seats);
        }

        scheduleTrainCoachRepo.delete(entity);

        return true;
    }
}