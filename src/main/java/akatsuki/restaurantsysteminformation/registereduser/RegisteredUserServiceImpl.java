package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.exception.RegisteredUserDeleteException;
import akatsuki.restaurantsysteminformation.role.Role;
import akatsuki.restaurantsysteminformation.role.RoleRepository;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.salary.SalaryService;
import akatsuki.restaurantsysteminformation.user.User;
import akatsuki.restaurantsysteminformation.user.UserService;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisteredUserServiceImpl implements RegisteredUserService {
    private final RegisteredUserRepository registeredUserRepository;
    private final UserService userService;
    private final SalaryService salaryService;
    private final RoleRepository roleRepository;

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

    //TODO Dodaj rolu
    @Override
    public RegisteredUser create(RegisteredUser registeredUser) {
        checkUsernameExistence(registeredUser.getUsername());
        userService.checkEmailExistence(registeredUser.getEmailAddress());
        userService.checkPhoneNumberExistence(registeredUser.getPhoneNumber());
        checkUserType(registeredUser.getType());

        Salary salary = salaryService.create(registeredUser.getSalary().get(0));
        registeredUser.setSalary(Collections.singletonList(salary));

        Role role = roleRepository.findByName(registeredUser.getType().toString()).get();
        registeredUser.setRole(role);

        return registeredUserRepository.save(registeredUser);
    }

    @Override
    public RegisteredUser update(RegisteredUser registeredUser, long id) {
        RegisteredUser user = getOne(id);
        validateUpdate(id, registeredUser, user.getType());

        user.setFirstName(registeredUser.getFirstName());
        user.setLastName(registeredUser.getLastName());
        user.setEmailAddress(registeredUser.getEmailAddress());
        user.setPhoneNumber(registeredUser.getPhoneNumber());

        if(!registeredUser.getSalary().isEmpty()) {
            Salary salary = salaryService.create(registeredUser.getSalary().get(0));
            List<Salary> salaries = user.getSalary();
            salaries.add(salary);
            user.setSalary(salaries);
        }

        user.setPassword(registeredUser.getPassword());

        return registeredUserRepository.save(user);
    }

    @Override
    public RegisteredUser delete(long id) {
        RegisteredUser user = getOne(id);
        if (user.getType().equals(UserType.ADMIN))
            throw new RegisteredUserDeleteException("User with the id " + id + " cannot be deleted.");
        user.setDeleted(true);
        return registeredUserRepository.save(user);
    }

    @Override
    public void save(RegisteredUser foundUser) {
        registeredUserRepository.save(foundUser);
    }

    private void validateUpdate(long id, RegisteredUser registeredUser, UserType oldType) {
        checkUserType(registeredUser.getType());
        if (!oldType.equals(registeredUser.getType())) {
            throw new UserTypeNotValidException("User type " + oldType.name().toLowerCase() + " cannot be changed to " + registeredUser.getType().name().toLowerCase() + ".");
        }
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
