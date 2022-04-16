package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomberg.fun.spring.air_flights.entity.Flight;
import tomberg.fun.spring.air_flights.entity.User;

import java.util.List;

public interface FlightsRepository extends JpaRepository<Flight, Integer> {
    List<Flight> findAllByUser(User user);
}
