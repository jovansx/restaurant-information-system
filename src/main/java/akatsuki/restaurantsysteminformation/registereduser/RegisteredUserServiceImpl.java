package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserChangePasswordDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.registereduser.exception.RegisteredUserDeleteException;
import akatsuki.restaurantsysteminformation.registereduser.exception.RegisteredUserPasswordException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final PasswordEncoder passwordEncoder;

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
    public RegisteredUser create(RegisteredUserDTO registeredUserDTO) {
        checkUsernameExistence(registeredUserDTO.getUsername());
        userService.checkEmailExistence(registeredUserDTO.getEmailAddress());
        userService.checkPhoneNumberExistence(registeredUserDTO.getPhoneNumber());
        checkUserType(registeredUserDTO.getType());

        Salary salary = salaryService.create(new Salary(LocalDateTime.now(), registeredUserDTO.getSalary()));
        Role role = roleRepository.findByName(registeredUserDTO.getType().toString()).get();

        RegisteredUser user = new RegisteredUser(registeredUserDTO.getFirstName(),
                registeredUserDTO.getLastName(), registeredUserDTO.getEmailAddress(),
                registeredUserDTO.getPhoneNumber(),
                Collections.singletonList(salary),
                registeredUserDTO.getType(), false,
                registeredUserDTO.getUsername(),
                registeredUserDTO.getPassword(), role);

        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);

        return registeredUserRepository.save(user);
    }

    @Override
    public RegisteredUser update(RegisteredUserDTO registeredUserDTO, long id) {
        RegisteredUser user = getOne(id);
        validateUpdate(id, registeredUserDTO, user.getType());

        user.setFirstName(registeredUserDTO.getFirstName());
        user.setLastName(registeredUserDTO.getLastName());
        user.setEmailAddress(registeredUserDTO.getEmailAddress());
        user.setPhoneNumber(registeredUserDTO.getPhoneNumber());

        if (registeredUserDTO.getSalary() != 0) {
            Salary salary = salaryService.create(new Salary(LocalDateTime.now(), registeredUserDTO.getSalary()));
            List<Salary> salaries = user.getSalary();
            salaries.add(salary);
            user.setSalary(salaries);
        }

        user.setPassword(registeredUserDTO.getPassword());

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
    public RegisteredUser changePassword(RegisteredUserChangePasswordDTO dto, long id) {
        RegisteredUser foundUser = getOne(id);

        String newPasswordHash = passwordEncoder.encode(dto.getNewPassword());
        if (!passwordEncoder.matches(dto.getOldPassword(), foundUser.getPassword())) {
            throw new RegisteredUserPasswordException("Old password " + dto.getOldPassword() + " not matches with recorded user password!");
        }
        foundUser.setPassword(newPasswordHash);
        registeredUserRepository.save(foundUser);
        return foundUser;
    }

    @Override
    public void deleteById(long id) {
        registeredUserRepository.deleteById(id);
    }

    @Override
    public void save(RegisteredUser foundUser) {
        registeredUserRepository.save(foundUser);
    }

    private void validateUpdate(long id, RegisteredUserDTO registeredUser, UserType oldType) {
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
