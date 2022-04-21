package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomberg.fun.spring.air_flights.entity.location.Airport;

import java.util.Set;

public interface AirportRepository extends JpaRepository<Airport, Integer> {
    Airport findByCountry(String country);
    Airport findByAirportCode(String airportCode);
}