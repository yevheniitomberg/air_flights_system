package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomberg.fun.spring.air_flights.entity.Flight;
import tomberg.fun.spring.air_flights.entity.Regulator;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    void deleteAllByRegulator(Regulator regulator);
    List<Flight> findAllByRegulator(Regulator regulator);
    Flight findByDepDateAndRegulator(LocalDate date, Regulator regulator);
}