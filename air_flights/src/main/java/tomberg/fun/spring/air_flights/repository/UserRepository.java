package tomberg.fun.spring.air_flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomberg.fun.spring.air_flights.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByConfirmLink(String confirmLink);
}
