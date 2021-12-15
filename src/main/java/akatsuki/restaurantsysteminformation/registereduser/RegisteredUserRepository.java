package akatsuki.restaurantsysteminformation.registereduser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {
    Optional<RegisteredUser> findByUsername(String username);

    @Query("select u from RegisteredUser u where u.type = 2")
    List<RegisteredUser> findAllSystemAdmins();
}
