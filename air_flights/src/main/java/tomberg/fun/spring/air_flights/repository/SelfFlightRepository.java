package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomberg.fun.spring.air_flights.entity.SelfFlight;
import tomberg.fun.spring.air_flights.entity.User;

public interface SelfFlightRepository extends JpaRepository<SelfFlight, Integer> {

    SelfFlight findByUserAndPaidFalse(User user);
}