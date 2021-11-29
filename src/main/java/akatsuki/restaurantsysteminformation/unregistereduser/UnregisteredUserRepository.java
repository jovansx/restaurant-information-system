package akatsuki.restaurantsysteminformation.unregistereduser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnregisteredUserRepository extends JpaRepository<UnregisteredUser, Long> {
    Optional<UnregisteredUser> findByPinCode(String pinCode);

}
