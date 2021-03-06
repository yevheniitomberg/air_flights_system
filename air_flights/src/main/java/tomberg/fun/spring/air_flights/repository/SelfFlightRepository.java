package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tomberg.fun.spring.air_flights.entity.SelfFlight;
import tomberg.fun.spring.air_flights.entity.User;

import java.util.List;

public interface SelfFlightRepository extends JpaRepository<SelfFlight, Integer> {
    SelfFlight findByUserAndPaidFalse(User user);
    List<SelfFlight> findAllByUserAndPaidFalse(User user);
    List<SelfFlight> findAllByUserAndPaidTrueOrderByDepDateDesc(User user);
}