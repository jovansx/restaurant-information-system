package akatsuki.restaurantsysteminformation.user;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    void checkEmailExistence(String email);
    void checkPhoneNumberExistence(String phoneNumber);
    void deleteValidation(long id);
}
