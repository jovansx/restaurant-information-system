package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.exception.RegisteredUserDeleteException;
import akatsuki.restaurantsysteminformation.role.Role;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisteredUserServiceIntegrationTest {

    @Autowired
    RegisteredUserServiceImpl registeredUserService;

    @Test
    public void getOne_ValidId_ReturnedObject() {
        RegisteredUser foundRegisteredUser = registeredUserService.getOne(10L);
        Assertions.assertNotNull(foundRegisteredUser);
    }

    @Test
    public void getOne_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(UserNotFoundException.class, () -> registeredUserService.getOne(8000L));
    }

    //TODO: ispraviti
//    @Test
//    void create_ValidEntity_SavedObject() {
//        RegisteredUser user = new RegisteredUser("Michael", "Lock", "michaellock@gmail.com",
//                "0645678822", null, UserType.MANAGER, false, "michael", "lock", new Role());
//        RegisteredUser savedUser = registeredUserService.create(user);
//        Assertions.assertNotNull(savedUser);
//    }

    @Test
    void create_AlreadyExistUsername_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(2L, "Michael", "Lock", "michaellock@gmail.com",
                "0645678822", null, UserType.MANAGER, false, "bradpitt", "lock", new Role());
        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.create(user));
    }

    @Test
    void create_InvalidUserType_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(2L, "Michael", "Lock", "michaellock@gmail.com",
                "0645678822", null, UserType.ADMIN, false, "michael123", "lock", new Role());
        Assertions.assertThrows(UserTypeNotValidException.class, () -> registeredUserService.create(user));
    }

    @Test
    public void update_ValidEntityAndId_SavedObject() {
        RegisteredUser user = new RegisteredUser("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", Collections.singletonList(new Salary(LocalDateTime.now(), 1000)), UserType.SYSTEM_ADMIN, false, "michael123", "lock", new Role());
        RegisteredUser updatedUser = registeredUserService.update(user, 11L);
        Assertions.assertNotNull(updatedUser);
    }

    @Test
    public void update_ChangedUserType_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(2L, "Michael", "Lock", "michaellock@gmail.com",
                "0645678822", null, UserType.MANAGER, false, "michael123", "lock", new Role());

        Assertions.assertThrows(UserTypeNotValidException.class, () -> registeredUserService.update(user, 10L));
    }

    @Test
    public void update_UsernameAlreadyExist_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser("Michael", "Lock", "bradpitt@gmail.com",
                "0645678822", null, UserType.SYSTEM_ADMIN, false, "michael123", "lock", new Role());

        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.update(user, 11L));
    }

    @Test
    public void update_EmailAlreadyExist_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser("Michael", "Lock", "calikikoki@gmail.com",
                "0645678822", null, UserType.SYSTEM_ADMIN, false, "michael123", "lock", new Role());
        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.update(user, 11L));
    }

    @Test
    public void update_PhoneNumberAlreadyExist_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(2L, "Michael", "Lock", "michaellock@gmail.com",
                "0611111114", null, UserType.SYSTEM_ADMIN, false, "michael123", "lock", new Role());
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