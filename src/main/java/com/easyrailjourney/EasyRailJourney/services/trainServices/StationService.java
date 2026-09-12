
package com.easyrailjourney.EasyRailJourney.services.trainServices;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.StationDtos.StationCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.StationDtos.StationDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.StationDtos.StationUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.models.City;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Station;
import com.easyrailjourney.EasyRailJourney.repository.CityRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.StationRepo;



@Service
public class StationService {

    private final StationRepo stationRepo;
    private final CityRepo cityRepo;
 

    public StationService(
            StationRepo stationRepo,
            CityRepo cityRepo
            ) {

        this.stationRepo = stationRepo;
        this.cityRepo = cityRepo;

    }

    // CREATE
    public Station createStation(
            StationCreateReqDto reqDto) throws Exception {

        if (reqDto.getCode() == null ||
                reqDto.getCode().isBlank()) {

            throw new Exception("Station code is required");
        }

        if (reqDto.getName() == null ||
                reqDto.getName().isBlank()) {

            throw new Exception("Station name is required");
        }

        if (stationRepo.findByCode(reqDto.getCode()).isPresent()) {

            throw new Exception("Station code already exists");
        }

        City city = cityRepo.findById(reqDto.getCity_id())
                .orElseThrow(() ->
                        new Exception("City not found"));


        Station station = new Station();

        station.setCode(reqDto.getCode());
        station.setName(reqDto.getName());

        station.setCity(city);


        station.setLatitude(reqDto.getLatitude());
        station.setLongitude(reqDto.getLongitude());

        station.setStatus(reqDto.getStatus());
        station.setIsDeleted(false);

        return stationRepo.save(station);
    }

    // GET ALL
    public List<Station> getAllStations() {
        return stationRepo.findAll();
    }

    // SEARCH
    public List<Station> searchStation(
            Long id,
            String code,
            String name,
            boolean isDeleted) {

        return stationRepo.searchStation(
                id,
                code,
                name,
                isDeleted
        );
    }

    // UPDATE
    public boolean updateStation(
            StationUpdateReqDto reqDto) throws Exception {

        Station station =
                stationRepo.findById(reqDto.getId())
                        .orElseThrow(() ->
                                new Exception("Station not found"));

        if (reqDto.getCode() != null &&
                !reqDto.getCode().isBlank() &&
                !reqDto.getCode().equals(station.getCode())) {

            Optional<Station> existing =
                    stationRepo.findByCode(reqDto.getCode());

            if (existing.isPresent() &&
                    !existing.get().getId()
                            .equals(station.getId())) {

                throw new Exception(
                        "Station code already exists"
                );
            }

            station.setCode(reqDto.getCode());
        }

        if (reqDto.getName() != null) {
            station.setName(reqDto.getName());
        }

        /*
         * Your DTO uses primitive long.
         * Therefore 0 means "not provided".
         */

        if (reqDto.getCity() != 0) {

            City city = cityRepo.findById(reqDto.getCity())
                    .orElseThrow(() ->
                            new Exception("City not found"));

            station.setCity(city);
        }

       

    

        if (reqDto.getLatitude() != null) {
            station.setLatitude(reqDto.getLatitude());
        }

        if (reqDto.getLongitude() != null) {
            station.setLongitude(reqDto.getLongitude());
        }

        station.setStatus(reqDto.getStatus());

        stationRepo.save(station);

        return true;
    }

    // SOFT DELETE
    public boolean deleteStation(
            StationDeleteReqDto reqDto) throws Exception {

        Station station = findStationForDelete(reqDto);

        station.setIsDeleted(true);

        stationRepo.save(station);

        return true;
    }

    // PERMANENT DELETE
    public boolean deleteStationPermanently(
            StationDeleteReqDto reqDto) throws Exception {

        Station station = findStationForDelete(reqDto);

        stationRepo.delete(station);

        return true;
    }

    private Station findStationForDelete(
            StationDeleteReqDto reqDto) throws Exception {

        if (reqDto.getId() != null) {

            return stationRepo.findById(reqDto.getId())
                    .orElseThrow(() ->
                            new Exception("Station not found"));
        }

        if (reqDto.getCode() != null &&
                !reqDto.getCode().isBlank()) {

            return stationRepo.findByCode(reqDto.getCode())
                    .orElseThrow(() ->
                            new Exception("Station not found"));
        }

        throw new Exception(
                "Provide station id or station code"
        );
    }
}