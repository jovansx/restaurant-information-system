package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.user.User;
import akatsuki.restaurantsysteminformation.user.UserService;
import akatsuki.restaurantsysteminformation.user.exception.UserDeletedException;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnregisteredUserServiceImpl implements UnregisteredUserService {
    private final UnregisteredUserRepository unregisteredUserRepository;
    private final UserService userService;

    @Override
    public UnregisteredUser getOne(long id) {
        return unregisteredUserRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with the id " + id + " is not found in the database.")
        );
    }

    @Override
    //TODO nije validiran broj telefona dal vec postoji takav
    public void create(UnregisteredUser unregisteredUser) {
        checkPinCodeExistence(unregisteredUser.getPinCode());
        userService.checkEmailExistence(unregisteredUser.getEmailAddress());
        checkUserType(unregisteredUser.getType());
        unregisteredUserRepository.save(unregisteredUser);
    }

    private void checkUserType(UserType type) {
        if (type != UserType.WAITER && type != UserType.CHEF && type != UserType.BARTENDER) {
            throw new UserTypeNotValidException("User type for unregistered user is not valid.");
        }
    }

    @Override
    public void delete(long id) {
        userService.deleteValidation(id);
        Optional<UnregisteredUser> user = unregisteredUserRepository.findById(id);
        // TODO sta ako imaju stavke

        user.get().setDeleted(true);
        unregisteredUserRepository.save(user.get());
    }

    // TODO ovde treba onaj where
    @Override
    public void update(UnregisteredUser unregisteredUser, long id) {
        Optional<UnregisteredUser> user = unregisteredUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with the id " + id + " is not found in the database.");
        } else if (user.get().isDeleted()) {
            throw new UserDeletedException("User with the id " + id + " is deleted.");
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
        checkUserType(unregisteredUser.getType());
        Optional<UnregisteredUser> userByPinCode = unregisteredUserRepository.findByPinCode(unregisteredUser.getPinCode());
        Optional<User> userByEmail = userService.findByEmail(unregisteredUser.getEmailAddress());
        Optional<User> userByPhoneNumber = userService.findByPhoneNumber(unregisteredUser.getPhoneNumber());

        if (userByPinCode.isPresent() && id != userByPinCode.get().getId()) {
            throw new UserExistsException("User with the pin code " + unregisteredUser.getPinCode() + " already exists in the database.");
        }

        if (userByEmail.isPresent() && id != userByEmail.get().getId()) {
            throw new UserExistsException("User with the email " + unregisteredUser.getEmailAddress() + " already exists in the database.");
        }

        if (userByPhoneNumber.isPresent() && id != userByPhoneNumber.get().getId()) {
            throw new UserExistsException("User with the phone number " + unregisteredUser.getPhoneNumber() + " already exists in the database.");
        }
    }

    private void checkPinCodeExistence(String pinCode) {
        Optional<UnregisteredUser> user = unregisteredUserRepository.findByPinCode(pinCode);
        if (user.isPresent()) {
            throw new UserExistsException("User with the pin code " + pinCode + " already exists in the database.");
        }
    }

    @Override
    public List<UnregisteredUser> getAll() {
        return unregisteredUserRepository.findAll();
    }

    @Override
    public UnregisteredUser checkPinCode(String pinCode, UserType type) {
        UnregisteredUser user = unregisteredUserRepository.findByPinCode(pinCode)
                .orElseThrow(() -> new UserNotFoundException("User with the pin code " + pinCode + " is not found in the database."));
        if (!user.getType().equals(type))
            throw new UserNotFoundException("User with the pin code " + pinCode + " is not a " + type.name().toLowerCase());
        return user;
    }

}
