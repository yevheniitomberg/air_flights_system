package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tomberg.fun.spring.air_flights.entity.Regulator;
import tomberg.fun.spring.air_flights.entity.Schedule;
import tomberg.fun.spring.air_flights.entity.time.Day;

import java.util.List;

public interface RegulatorRepository extends JpaRepository<Regulator, Integer> {
    List<Regulator> findAllBySchedule(Schedule schedule);
    void deleteAllBySchedule(Schedule schedule);
    Regulator findBySchedule(Schedule schedule);
    void deleteAllByScheduleAndValidFromDateIsNull(Schedule schedule);
    Regulator findByScheduleAndDay(Schedule schedule, Day day);
}