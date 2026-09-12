package com.easyrailjourney.EasyRailJourney.services.trainServices;


import java.util.List;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.SeatType.SeatTypeReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.SeatType.SeatTypeRespDto;



import org.springframework.stereotype.Service;


import com.easyrailjourney.EasyRailJourney.models.trainOperation.SeatType;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.SeatTypeRepo;

@Service
public class SeatTypeService {

    private final SeatTypeRepo seatTypeRepository;

    public SeatTypeService(SeatTypeRepo seatTypeRepository) {
        this.seatTypeRepository = seatTypeRepository;
    }

    // CREATE SEAT TYPE

    public SeatType createSeatType(SeatTypeReqDto reqDto) {

        if (reqDto == null) {
            throw new RuntimeException("Request cannot be null");
        }

        if (seatTypeRepository.existsByTypeCode(reqDto.getTypeCode())) {
            throw new RuntimeException(
                    "Seat type already exists with type code: "
                            + reqDto.getTypeCode()
            );
        }

        SeatType seatType = new SeatType();

        seatType.setTypeCode(reqDto.getTypeCode());
        seatType.setTypeName(reqDto.getTypeName());
        seatType.setDescription(reqDto.getDescription());
        seatType.setDeleted(false);

        return seatTypeRepository.save(seatType);
    }


    // GET ALL SEAT TYPES

    public List<SeatType> getAllSeatTypes() {

        return seatTypeRepository.findAll();
    }


    // SEARCH SEAT TYPE

    public List<SeatType> searchSeatType(
            Long id,
            String typeCode,
            String typeName,
            boolean isDeleted) {

        return seatTypeRepository.searchSeatType(
                id,
                typeCode,
                typeName,
                isDeleted
        );
    }


    // UPDATE SEAT TYPE

    public boolean updateSeatType(SeatTypeReqDto reqDto) {

        if (reqDto == null) {
            throw new RuntimeException("Request cannot be null");
        }

        SeatType seatType = seatTypeRepository
                .findById(reqDto.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Seat type not found with id: "
                                        + reqDto.getId()
                        )
                );

        if (seatType.isDeleted()) {
            throw new RuntimeException(
                    "Cannot update a deleted seat type"
            );
        }

        seatType.setTypeCode(reqDto.getTypeCode());
        seatType.setTypeName(reqDto.getTypeName());
        seatType.setDescription(reqDto.getDescription());

        seatTypeRepository.save(seatType);

        return true;
    }


    // SOFT DELETE SEAT TYPE
    public boolean deleteSeatType(SeatTypeReqDto reqDto) {

        if (reqDto == null) {
            throw new RuntimeException("Request cannot be null");
        }

        SeatType seatType = seatTypeRepository
                .findById(reqDto.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Seat type not found with id: "
                                        + reqDto.getId()
                        )
                );

        if (seatType.isDeleted()) {
            return false;
        }

        seatType.setDeleted(true);

        seatTypeRepository.save(seatType);

        return true;
    }


    // PERMANENT DELETE SEAT TYPE

    public boolean deleteSeatTypePermanently(
        SeatTypeReqDto reqDto) {

        if (reqDto == null) {
            throw new RuntimeException("Request cannot be null");
        }

        SeatType seatType = seatTypeRepository
                .findById(reqDto.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Seat type not found with id: "
                                        + reqDto.getId()
                        )
                );

        seatTypeRepository.delete(seatType);

        return true;
    }
}