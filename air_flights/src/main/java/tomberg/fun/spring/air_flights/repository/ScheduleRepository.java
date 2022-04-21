package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomberg.fun.spring.air_flights.entity.Route;
import tomberg.fun.spring.air_flights.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Schedule findByRoute(Route route);
}