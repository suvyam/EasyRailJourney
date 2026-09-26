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
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainStation;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Station;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingsRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainStationRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.StationRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainRepo;
import com.easyrailjourney.EasyRailJourney.services.TicketService;

import jakarta.transaction.Transactional;


@Service
public class ScheduleTrainService {

    private final ScheduleTrainRepo scheduleRepository;
    private final TrainRepo trainRepo;
    private final StationRepo stationRepo;
    private final ScheduleTrainHistoryService scheduleTrainHistoryService;
    private final ScheduleTrainStationService scheduleTrainStationHistoryService;
    private final ScheduleTrainRescheduleEventService scheduleTrainRescheduleEventService;
    private final BookingsRepo bookingsRepo;
    private final TicketService ticketService;
    private final ScheduleTrainStationRepo scheduleTrainStationRepo;


    public ScheduleTrainService(
            ScheduleTrainRepo scheduleRepository,
            TrainRepo trainRepo,
            StationRepo stationRepo,
            ScheduleTrainHistoryService scheduleTrainHistoryService,
            ScheduleTrainStationService scheduleTrainStationHistoryService,
            ScheduleTrainRescheduleEventService scheduleTrainRescheduleEventService,
            BookingsRepo bookingsRepo,
            TicketService ticketService,
            ScheduleTrainStationRepo scheduleTrainStationRepo) {

        this.scheduleRepository = scheduleRepository;
        this.trainRepo = trainRepo;
        this.stationRepo = stationRepo;
        this.scheduleTrainHistoryService = scheduleTrainHistoryService;
        this.scheduleTrainStationHistoryService= scheduleTrainStationHistoryService;
        this.scheduleTrainRescheduleEventService = scheduleTrainRescheduleEventService;
        this.bookingsRepo = bookingsRepo;
        this.ticketService = ticketService;
        this.scheduleTrainStationRepo = scheduleTrainStationRepo;
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Transactional 
    public ScheduleRespDto createSchedule(
            ScheduleCreateReqDto reqDto) throws Exception {

        // Here you will find Train and Stations using their IDs

        Train train = trainRepo.findById(reqDto.getTrainId())
                .orElseThrow(() -> new Exception("Train not found"));


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
        schedule.setJourneyEstimatedEndTime(reqDto.getJourneyEstimatedEndTime());
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
                        status,
                        isDeleted
                );

        return wrapper(list);
    }

    // =====================================================
    // UPDATE
    // =====================================================


    

    @Transactional(rollbackOn = Exception.class)
public boolean updateSchedule(
        ScheduleUpdateReqDto reqDto)
        throws Exception {


    // =====================================================
    // 1. FIND SCHEDULE
    // =====================================================

    ScheduleTrain schedule =
            scheduleRepository.findById(
                    reqDto.getId()
            ).orElseThrow(() ->
                    new Exception(
                            "Schedule not found to update"
                    )
            );


    // =====================================================
    // 2. FINAL TRAIN
    // =====================================================

    Train finalTrain =
            schedule.getTrain();

    if (reqDto.getTrainId() != null) {

        finalTrain =
                trainRepo.findById(
                        reqDto.getTrainId()
                ).orElseThrow(() ->
                        new Exception(
                                "Train not found"
                        )
                );
    }


    // =====================================================
    // 3. FINAL DEPARTURE STATION
    // =====================================================

    Station finalDepartureStation =
            schedule.getDepartureStation();

    if (reqDto.getDepartureStationId() != null) {

        finalDepartureStation =
                stationRepo.findById(
                        reqDto.getDepartureStationId()
                ).orElseThrow(() ->
                        new Exception(
                                "Departure station not found"
                        )
                );
    }


    // =====================================================
    // 4. FINAL DESTINATION STATION
    // =====================================================

    Station finalDestinationStation =
            schedule.getDestinationStation();

    if (reqDto.getDestinationStationId() != null) {

        finalDestinationStation =
                stationRepo.findById(
                        reqDto.getDestinationStationId()
                ).orElseThrow(() ->
                        new Exception(
                                "Destination station not found"
                        )
                );
    }


    // =====================================================
    // 5. OLD TIMES
    // =====================================================

    Date oldStartTime =
            schedule.getJourneyStartTime();

    Date oldEndTime =
            schedule.getJourneyEstimatedEndTime();

    if (oldStartTime == null
            || oldEndTime == null) {

        throw new Exception(
                "Existing journey start/end time is required"
        );
    }


    // =====================================================
    // 6. FINAL TIMES
    // =====================================================

    Date finalStartTime =
            reqDto.getJourneyStartTime() != null
                    ? reqDto.getJourneyStartTime()
                    : oldStartTime;

    Date finalEndTime =
            reqDto.getJourneyEstimatedEndTime() != null
                    ? reqDto.getJourneyEstimatedEndTime()
                    : oldEndTime;


    // =====================================================
    // 7. VALIDATE TIME
    // =====================================================

    if (!finalStartTime.before(finalEndTime)) {

        throw new Exception(
                "Journey start time must be before journey estimated end time"
        );
    }


    // =====================================================
    // 8. CHECK WHETHER TIME CHANGED
    // =====================================================

    boolean journeyTimeChanged =
            !oldStartTime.equals(finalStartTime)
                    || !oldEndTime.equals(finalEndTime);


    // =====================================================
    // 9. CALCULATE DELTA
    // =====================================================

    long deltaMillis =
            finalStartTime.getTime()
                    - oldStartTime.getTime();


    // =====================================================
    // 10. CHECK OVERLAP
    // =====================================================

    List<ScheduleTrain> overlappingSchedules =
            scheduleRepository.findOverlappingSchedules(

                    finalTrain.getId(),

                    finalStartTime,

                    finalEndTime
            );


    for (ScheduleTrain existingSchedule :
            overlappingSchedules) {


        if (existingSchedule.getId()
                .equals(schedule.getId())) {

            continue;
        }


        throw new Exception(
                "Train already has an overlapping schedule "
                        + "for this time. Conflicting schedule id: "
                        + existingSchedule.getId()
        );
    }


    // =====================================================
    // 11. GET STATIONS
    // =====================================================

    List<ScheduleTrainStation> scheduleStations =
            scheduleTrainStationRepo
                    .findByScheduleTrain(
                            schedule
                    );


    // =====================================================
    // 12. GET BOOKINGS
    // =====================================================

    List<Bookings> bookings =
            bookingsRepo.findByScheduleTrainId(
                    schedule.getId()
            );


    if (bookings == null) {

        bookings =
                new ArrayList<>();
    }


    // =====================================================
    // 13. ARCHIVE OLD SCHEDULE
    // =====================================================

    if (journeyTimeChanged) {

        scheduleTrainHistoryService.archive(
                schedule
        );
    }


    // =====================================================
    // 14. ARCHIVE OLD STATIONS
    // =====================================================

    if (journeyTimeChanged) {

        for (ScheduleTrainStation station :
                scheduleStations) {


            if (Boolean.TRUE.equals(
                    station.getIsDeleted())) {

                continue;
            }


            scheduleTrainStationHistoryService.archive(
                    station
            );
        }
    }


    // =====================================================
    // 15. UPDATE SCHEDULE
    // =====================================================

    if (reqDto.getTrainId() != null) {

        schedule.setTrain(
                finalTrain
        );
    }


    if (reqDto.getDepartureStationId() != null) {

        schedule.setDepartureStation(
                finalDepartureStation
        );
    }


    if (reqDto.getDestinationStationId() != null) {

        schedule.setDestinationStation(
                finalDestinationStation
        );
    }


    if (reqDto.getJourneyStartTime() != null) {

        schedule.setJourneyStartTime(
                reqDto.getJourneyStartTime()
        );
    }


    if (reqDto.getJourneyEstimatedEndTime() != null) {

        schedule.setJourneyEstimatedEndTime(
                reqDto.getJourneyEstimatedEndTime()
        );
    }


    if (reqDto.getStatus() != null) {

        schedule.setStatus(
                reqDto.getStatus()
        );
    }


    // =====================================================
    // 16. SHIFT STATION TIMES
    // =====================================================

    if (journeyTimeChanged
            && deltaMillis != 0) {


        for (ScheduleTrainStation station :
                scheduleStations) {


            if (Boolean.TRUE.equals(
                    station.getIsDeleted())) {

                continue;
            }


            if (station.getArrivalTime() != null) {

                station.setArrivalTime(

                        new Date(
                                station.getArrivalTime()
                                        .getTime()
                                        + deltaMillis
                        )
                );
            }


            if (station.getDepartureTime() != null) {

                station.setDepartureTime(

                        new Date(
                                station.getDepartureTime()
                                        .getTime()
                                        + deltaMillis
                        )
                );
            }
        }
    }


    // =====================================================
    // 17. UPDATE BOOKING JOURNEY DATE
    // =====================================================

    if (journeyTimeChanged
            && deltaMillis != 0) {


        for (Bookings booking :
                bookings) {


            if (booking == null) {
                continue;
            }


            if (booking.getJourneyDate() == null) {
                continue;
            }


            booking.setJourneyDate(

                    new Date(
                            booking.getJourneyDate()
                                    .getTime()
                                    + deltaMillis
                    )
            );
        }
    }


    // =====================================================
    // 18. UPDATE TICKET SNAPSHOT
    // =====================================================

    if (journeyTimeChanged
            && deltaMillis != 0) {


        for (Bookings booking :
                bookings) {


            if (booking == null) {
                continue;
            }


            if (booking.getPnr() == null
                    || booking.getPnr().isBlank()) {

                continue;
            }


            ticketService.updateTicketTimes(
                    booking.getPnr(),
                    deltaMillis
            );
        }
    }


    // =====================================================
    // 19. SAVE SCHEDULE
    // =====================================================

    scheduleRepository.saveAndFlush(
            schedule
    );


    // =====================================================
    // 20. SAVE STATIONS
    // =====================================================

    if (!scheduleStations.isEmpty()) {

        scheduleTrainStationRepo
                .saveAllAndFlush(
                        scheduleStations
                );
    }


    // =====================================================
    // 21. SAVE BOOKINGS
    // =====================================================

    if (!bookings.isEmpty()) {

        bookingsRepo.saveAllAndFlush(
                bookings
        );
    }


    // =====================================================
    // 22. CREATE EVENT + OUTBOX
    //
    // IMPORTANT:
    //
    // This does NOT send email.
    //
    // It only stores:
    //
    // RescheduleEvent
    // +
    // Outbox records
    //
    // Both are committed together with the schedule update.
    // =====================================================

    if (journeyTimeChanged) {

        scheduleTrainRescheduleEventService
                .createEventAndOutbox(

                        schedule.getId(),

                        oldStartTime,
                        oldEndTime,

                        finalStartTime,
                        finalEndTime,

                        "Schedule train time updated"
                );
    }


    // =====================================================
    // 23. COMMIT AFTER SUCCESSFUL RETURN
    // =====================================================

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

@Transactional 
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