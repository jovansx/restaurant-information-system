package akatsuki.restaurantsysteminformation.user;

import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailAddress(email);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public void checkEmailExistence(String email) {
        Optional<User> user = findByEmail(email);
        if (user.isPresent())
            throw new UserExistsException("User with the email " + email + " already exists in the database.");
    }

    @Override
    public void checkPhoneNumberExistence(String phoneNumber) {
        Optional<User> user = findByPhoneNumber(phoneNumber);
        if (user.isPresent())
            throw new UserExistsException("User with the phone number " + phoneNumber + " already exists in the database.");
    }
}
