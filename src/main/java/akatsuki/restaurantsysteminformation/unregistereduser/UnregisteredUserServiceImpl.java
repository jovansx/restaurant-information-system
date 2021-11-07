package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.user.User;
import akatsuki.restaurantsysteminformation.user.UserService;
import akatsuki.restaurantsysteminformation.user.exception.UserDeletedException;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnregisteredUserServiceImpl implements UnregisteredUserService {
    private final UnregisteredUserRepository unregisteredUserRepository;
    private final UserService userService;

    @Autowired
    public UnregisteredUserServiceImpl(UnregisteredUserRepository unregisteredUserRepository, UserService userService) {
        this.unregisteredUserRepository = unregisteredUserRepository;
        this.userService = userService;
    }

    @Override
    public UnregisteredUser getOne(long id) {
        return unregisteredUserRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public void create(UnregisteredUser unregisteredUser) {
        checkPinCodeExistence(unregisteredUser.getPinCode());
        checkEmailExistence(unregisteredUser.getEmailAddress());
        unregisteredUserRepository.save(unregisteredUser);
    }

    @Override
    public void delete(long id) {
        Optional<UnregisteredUser> user = unregisteredUserRepository.findById(id);
        // TODO sta ako imaju stavke
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with the id " + id + " is not found in the database.");
        }
        if (user.get().isDeleted()) {
            throw new UserDeletedException("User with the id " + id + " already deleted.");
        }
        user.get().setDeleted(true);
        unregisteredUserRepository.save(user.get());
    }

    @Override
    public void update(UnregisteredUser unregisteredUser, long id) {
        Optional<UnregisteredUser> user = unregisteredUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with the id " + id + " is not found in the database.");
        }
        validateUpdate(id, unregisteredUser);

        UnregisteredUser foundUser = user.get();
        foundUser.setFirstName(unregisteredUser.getFirstName());
        foundUser.setLastName(unregisteredUser.getLastName());
        foundUser.setEmailAddress(unregisteredUser.getEmailAddress());
        foundUser.setPhoneNumber(unregisteredUser.getPhoneNumber());
        foundUser.setSalary(unregisteredUser.getSalary());
        foundUser.setPinCode(unregisteredUser.getPinCode());

        unregisteredUserRepository.save(foundUser);
    }

    private void validateUpdate(long id, UnregisteredUser unregisteredUser) {
        Optional<UnregisteredUser> userByPinCode = unregisteredUserRepository.findByPinCode(unregisteredUser.getPinCode());
        Optional<User> userByEmail = userService.findByEmail(unregisteredUser.getEmailAddress());

        if (userByPinCode.isPresent() && id != userByPinCode.get().getId()) {
            throw new UserExistsException("User with the pin code " + unregisteredUser.getPinCode() + " already exists in the database.");
        }

        if (userByEmail.isPresent() && id != userByEmail.get().getId()) {
            throw new UserExistsException("User with the email " + unregisteredUser.getEmailAddress() + " already exists in the database.");
        }
    }

    private void checkPinCodeExistence(String pinCode) {
        Optional<UnregisteredUser> user = unregisteredUserRepository.findByPinCode(pinCode);
        if (user.isPresent()) {
            throw new UserExistsException("User with the pin code " + pinCode + " already exists in the database.");
        }
    }

    private void checkEmailExistence(String email) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            throw new UserExistsException("User with the email " + email + " already exists in the database.");
        }
    }

    @Override
    public List<UnregisteredUser> getAll() {
        return unregisteredUserRepository.findAll();
    }

}
