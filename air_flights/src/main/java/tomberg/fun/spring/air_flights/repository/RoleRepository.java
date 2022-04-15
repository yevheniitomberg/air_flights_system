package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomberg.fun.spring.air_flights.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findById(int id);
}
