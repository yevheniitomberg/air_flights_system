package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomberg.fun.spring.air_flights.entity.Plane;

public interface PlaneRepository extends JpaRepository<Plane, Integer> {

}