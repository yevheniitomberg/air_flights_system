package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tomberg.fun.spring.air_flights.entity.Route;
import tomberg.fun.spring.air_flights.entity.location.Airport;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Integer> {
    Route findByAirportFromAndAirportTo(Airport airportFrom, Airport airportTo);
    @Query(countQuery = "select r from Route r where r.airportFrom.id=:id_from and r.airportTo.id=:id_to")
    Route findByAirportFromIdAndAirportToId(@Param("id_from") int id_from, @Param("id_to") int id_to);
    @Query(value = "select r from Route r where r.airportFrom.id>1 and r.airportTo.id>1 order by r.airportFrom.id asc")
    List<Route> findAllWithoutDefault();
}