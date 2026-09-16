// package com.easyrailjourney.EasyRailJourney.services;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.stereotype.Service;

// import com.easyrailjourney.EasyRailJourney.Dtos.DataMeet.DataMeetStationDto;
// import com.easyrailjourney.EasyRailJourney.models.trainOperation.Station;
// import com.easyrailjourney.EasyRailJourney.repository.CityRepo;
// import com.easyrailjourney.EasyRailJourney.repository.StateRepo;
// import com.easyrailjourney.EasyRailJourney.repository.trainRepos.StationRepo;

// import jakarta.transaction.Transactional;
// import lombok.RequiredArgsConstructor;




// @Service
// @RequiredArgsConstructor
// public class DataMeetImportService {

//     private final StationRepo stationRepository;
//     private final StateRepo stateRepository;
//     private final CityRepo cityRepository;

//     @Transactional
//     public void importStations() {

//         // List<DataMeetStationDto> stations = readStationsJson();

//         for (DataMeetStationDto dto : stations) {

//             String code =
//                     dto.getProperties().getCode();

//             Optional<Station> existing =
//                     stationRepository.findByCode(code);

//             Station station =
//                     existing.orElseGet(Station::new);

//             station.setCode(code);

//             station.setName(
//                     dto.getProperties().getName()
//             );

//             station.setAddress(
//                     dto.getProperties().getAddress()
//             );

//             station.setLatitude(
//                     String.valueOf(
//                         dto.getGeometry()
//                            .getCoordinates()
//                            .get(1)
//                     )
//             );

//             station.setLongitude(
//                     String.valueOf(
//                         dto.getGeometry()
//                            .getCoordinates()
//                            .get(0)
//                     )
//             );

//             stationRepository.save(station);
//         }
//     }
// }