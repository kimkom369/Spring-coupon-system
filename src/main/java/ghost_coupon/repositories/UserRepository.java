package ghost_coupon.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ghost_coupon.entities.User;


public interface UserRepository extends JpaRepository<User, Long> {
    
	//------------------------------- select user from USER:name & USER:password -------------------------\\
    @Query("SELECT DISTINCT u FROM User u WHERE UPPER(u.name) LIKE UPPER(:name) AND u.password = :password")
    Optional<User> login(String name, String password);
}
