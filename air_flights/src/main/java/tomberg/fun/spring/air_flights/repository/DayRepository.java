package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomberg.fun.spring.air_flights.entity.time.Day;

public interface DayRepository extends JpaRepository<Day, Integer> {
}