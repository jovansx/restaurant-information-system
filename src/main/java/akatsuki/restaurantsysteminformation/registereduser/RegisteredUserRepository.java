package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {
    Optional<RegisteredUser> findByUsername(String username);

    @Query("select u from User u where u.type = 2")
    List<User> findAllSystemAdmins();
}
