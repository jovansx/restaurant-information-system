package akatsuki.restaurantsysteminformation.user;

import akatsuki.restaurantsysteminformation.user.exception.UserDeletedException;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        if (user.isPresent()) {
            throw new UserExistsException("User with the email " + email + " already exists in the database.");
        }
    }

    @Override
    public void checkPhoneNumberExistence(String phoneNumber) {
        Optional<User> user = findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            throw new UserExistsException("User with the phone number " + phoneNumber + " already exists in the database.");
        }
    }

    @Override
    public void deleteValidation(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with the id " + id + " is not found in the database.");
        }
        if (user.get().isDeleted()) {
            throw new UserDeletedException("User with the id " + id + " already deleted.");
        }
    }


}
