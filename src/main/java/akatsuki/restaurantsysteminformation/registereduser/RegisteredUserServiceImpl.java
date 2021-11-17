package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.exception.RegisteredUserDeleteException;
import akatsuki.restaurantsysteminformation.user.User;
import akatsuki.restaurantsysteminformation.user.UserService;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisteredUserServiceImpl implements RegisteredUserService {
    private final RegisteredUserRepository registeredUserRepository;
    private final UserService userService;

    @Override
    public RegisteredUser getOne(long id) {
        return registeredUserRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public List<RegisteredUser> getAll() {
        return registeredUserRepository.findAll();
    }

    @Override
    public void create(RegisteredUser registeredUser) {
        checkUsernameExistence(registeredUser.getUsername());
        userService.checkEmailExistence(registeredUser.getEmailAddress());
        userService.checkPhoneNumberExistence(registeredUser.getPhoneNumber());
        checkUserType(registeredUser.getType());
        registeredUserRepository.save(registeredUser);
    }

    @Override
    public void update(RegisteredUser registeredUser, long id) {
        RegisteredUser user = getOne(id);
        validateUpdate(id, registeredUser);

        user.setFirstName(registeredUser.getFirstName());
        user.setLastName(registeredUser.getLastName());
        user.setEmailAddress(registeredUser.getEmailAddress());
        user.setPhoneNumber(registeredUser.getPhoneNumber());
        user.setSalary(registeredUser.getSalary());
        user.setPassword(registeredUser.getPassword());

        registeredUserRepository.save(user);
    }

    @Override
    public void delete(long id) {
        RegisteredUser user = getOne(id);
        if (user.getType().equals(UserType.ADMIN))
            throw new RegisteredUserDeleteException("User with the id " + id + " cannot be deleted.");
        user.setDeleted(true);
        registeredUserRepository.save(user);
    }

    private void validateUpdate(long id, RegisteredUser registeredUser) {
        checkUserType(registeredUser.getType());
        Optional<RegisteredUser> userByUsername = registeredUserRepository.findByUsername(registeredUser.getUsername());
        Optional<User> userByEmail = userService.findByEmail(registeredUser.getEmailAddress());
        Optional<User> userByPhoneNumber = userService.findByPhoneNumber(registeredUser.getPhoneNumber());

        if (userByUsername.isPresent() && id != userByUsername.get().getId())
            throw new UserExistsException("User with the pin code " + registeredUser.getUsername() + " already exists in the database.");

        if (userByEmail.isPresent() && id != userByEmail.get().getId())
            throw new UserExistsException("User with the email " + registeredUser.getEmailAddress() + " already exists in the database.");

        if (userByPhoneNumber.isPresent() && id != userByPhoneNumber.get().getId())
            throw new UserExistsException("User with the phone number " + registeredUser.getPhoneNumber() + " already exists in the database.");
    }

    private void checkUsernameExistence(String username) {
        Optional<RegisteredUser> user = registeredUserRepository.findByUsername(username);
        if (user.isPresent())
            throw new UserExistsException("User with the username " + username + " already exists in the database.");
    }

    private void checkUserType(UserType type) {
        if (type != UserType.MANAGER && type != UserType.SYSTEM_ADMIN)
            throw new UserTypeNotValidException("User type for registered user is not valid.");
    }
}
