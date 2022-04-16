package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomberg.fun.spring.air_flights.entity.Gender;

public interface GenderRepository extends JpaRepository<Gender, Integer> {
}