package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDetailsDTO;
import akatsuki.restaurantsysteminformation.registereduser.exception.RegisteredUserDeleteException;
import akatsuki.restaurantsysteminformation.role.Role;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.user.User;
import akatsuki.restaurantsysteminformation.user.UserRepository;
import akatsuki.restaurantsysteminformation.user.UserService;
import akatsuki.restaurantsysteminformation.user.UserServiceImpl;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisteredUserServiceIntegrationTest {

    @Autowired
    RegisteredUserServiceImpl registeredUserService;

    @Autowired
    UserRepository userRepository;

    @Test
    public void getOne_ValidId_ReturnedObject() {
        RegisteredUser foundRegisteredUser = registeredUserService.getOne(10L);
        Assertions.assertNotNull(foundRegisteredUser);
    }

    @Test
    public void getOne_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(UserNotFoundException.class, () -> registeredUserService.getOne(8000L));
    }

    @Test
    void create_ValidEntity_SavedObject() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.MANAGER, "michael", "lock");
        RegisteredUser savedUser = registeredUserService.create(user);
        Assertions.assertNotNull(savedUser);
    }

    @Test
    void create_AlreadyExistUsername_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.MANAGER, "bradpitt", "lock");
        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.create(user));
    }

    @Test
    void create_InvalidUserType_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.ADMIN, "michael123", "lock");
        Assertions.assertThrows(UserTypeNotValidException.class, () -> registeredUserService.create(user));
    }

    @Test
    public void update_ValidEntityAndId_SavedObject() {
        RegisteredUserDetailsDTO user = new RegisteredUserDetailsDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 1300, UserType.SYSTEM_ADMIN, "michael123");
        RegisteredUser updatedUser = registeredUserService.update(user, 11L);
        Assertions.assertNotNull(updatedUser);
    }

    @Test
    public void update_ChangedUserType_ExceptionThrown() {
        RegisteredUserDetailsDTO user = new RegisteredUserDetailsDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.MANAGER, "michael123");
        Assertions.assertThrows(UserTypeNotValidException.class, () -> registeredUserService.update(user, 10L));
    }

    @Test
    public void update_UsernameAlreadyExist_ExceptionThrown() {
        RegisteredUserDetailsDTO user = new RegisteredUserDetailsDTO("Michael", "Lock", "bradpitt@gmail.com",
                "0645678822", 0, UserType.SYSTEM_ADMIN, "michael123");
        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.update(user, 11L));
    }

    @Test
    public void update_EmailAlreadyExist_ExceptionThrown() {
        RegisteredUserDetailsDTO user = new RegisteredUserDetailsDTO("Michael", "Lock", "calikikoki@gmail.com",
                "0645678822", 0, UserType.SYSTEM_ADMIN, "michael123");
        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.update(user, 11L));
    }

    @Test
    public void update_PhoneNumberAlreadyExist_ExceptionThrown() {
        RegisteredUserDetailsDTO user = new RegisteredUserDetailsDTO("Michael", "Lock", "michaellock@gmail.com",
                "0611111114", 0, UserType.SYSTEM_ADMIN, "michael123");
        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.update(user, 11L));
    }

    @Test
    public void delete_ValidId_SavedObject() {
        RegisteredUser user = registeredUserService.delete(11L);
        Assertions.assertNotNull(user);
    }

    @Test
    public void delete_InvalidType_ExceptionThrown() {
        Assertions.assertThrows(RegisteredUserDeleteException.class, () -> registeredUserService.delete(10L));
    }
}