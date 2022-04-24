package tomberg.fun.spring.air_flights.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tomberg.fun.spring.air_flights.entity.Flight;
import tomberg.fun.spring.air_flights.entity.location.Airport;
import tomberg.fun.spring.air_flights.repository.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    AirportRepository airportRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    RegulatorRepository regulatorRepository;

    @GetMapping("/book/{action}/{AirportCode}")
    @CrossOrigin(allowedHeaders = "*", origins = "http://localhost:8080/")
    public Set<Airport> airportConnectedSet(@PathVariable String action, @PathVariable String AirportCode) {

        if (action.equals("select")) {
            Airport airport = airportRepository.findByAirportCode(AirportCode);
            Set<Airport> set = airport.getConnected_airports();

            for (Airport airport1: set) {
                airport1.setConnected_airports(null);
            }
            set.remove(airportRepository.findByAirportCode("000"));
            return set;

        }


        if (action.equals("make_connections")) {
            Airport airport = airportRepository.findByAirportCode(AirportCode);
            Set<Airport> setAll = new HashSet<>(airportRepository.findAll());
            Set<Airport> set = airport.getConnected_airports();

            for (Airport airport1: set) {
                airport1.setConnected_airports(null);
            }
            for (Airport airport1: set) {
                setAll.remove(airport1);
            }
            setAll.remove(airport);
            return setAll;
        }
        return null;
    }

    @GetMapping("/dates/{from}/{to}")
    @CrossOrigin(allowedHeaders = "*", origins = "http://localhost:8080/")
    public Set<LocalDate> availableDates(@PathVariable String from, @PathVariable String to) {
        int from_id = airportRepository.findByAirportCode(from).getId();
        int to_id = airportRepository.findByAirportCode(to).getId();

        List<Flight> flightList = flightRepository.findAllByRegulator(regulatorRepository.findBySchedule(scheduleRepository.findByRoute(routeRepository.findByAirportFromIdAndAirportToId(from_id, to_id))));

        Set<LocalDate> set = new HashSet<>();
        for (Flight flight : flightList) {
            set.add(flight.getDepDate());
        }
        return set;
    }
}
