package akatsuki.restaurantsysteminformation.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAddress(String emailAddress);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("select u from User u where u.type = 1 or u.type = 3 or u.type = 4 or u.type = 5")
    List<User> findAllManagersAndUnregistered();
}
