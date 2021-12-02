package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.unregistereduser.exception.UnregisteredUserActiveException;
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
class UnregisteredUserServiceIntegrationTest {

    @Autowired
    UnregisteredUserServiceImpl unregisteredUserService;

    @Test
    void create_Valid_SavedObject() {
        UnregisteredUser user = new UnregisteredUser("Jelena", "Stojanovic", "sekica@gmail.com", "069121223", Collections.singletonList(new Salary(LocalDateTime.now(),1000000)), UserType.WAITER, false, "9999");
        UnregisteredUser createdUser = unregisteredUserService.create(user);
        Assertions.assertNotNull(createdUser);
    }

    @Test
    void create_InvalidPin_ExceptionThrown() {
        UnregisteredUser user = new UnregisteredUser("Jelena", "Stojanovic", "sekica@gmail.com", "069111223", Collections.singletonList(new Salary(LocalDateTime.now(),1000000)), UserType.WAITER, false, "1111");
        Assertions.assertThrows(UserExistsException.class, () -> unregisteredUserService.create(user));
    }

    @Test
    void create_InvalidUserType_ExceptionThrown() {
        UnregisteredUser user = new UnregisteredUser("Jelena", "Stojanovic", "sekica@gmail.com", "069111223", Collections.singletonList(new Salary(LocalDateTime.now(),1000000)), UserType.MANAGER, false, "8888");
        Assertions.assertThrows(UserTypeNotValidException.class, () -> unregisteredUserService.create(user));
    }

    @Test
    void update_Valid_SavedObject() {
        UnregisteredUser user = new UnregisteredUser("Simon", "Baker", "simonbaker@gmail.com", "0611111112", Collections.singletonList(new Salary(LocalDateTime.now(),1000000)), UserType.WAITER, false, "1112");
        UnregisteredUser updatedUser = unregisteredUserService.update(user, 2L);
        Assertions.assertNotNull(updatedUser);
    }

    @Test
    void update_InvalidPin_ExceptionThrown() {
        UnregisteredUser user = new UnregisteredUser("John", "Cena", "johncena@gmail.com", "069111223", Collections.singletonList(new Salary(LocalDateTime.now(),1000000)), UserType.WAITER, false, "1112");
        Assertions.assertThrows(UserExistsException.class, () -> unregisteredUserService.update(user, 1L));
    }

    @Test
    void update_InvalidUserType_ExceptionThrown() {
        UnregisteredUser user = new UnregisteredUser("John", "Cena", "johncena@gmail.com", "069111223", Collections.singletonList(new Salary(LocalDateTime.now(),1000000)), UserType.MANAGER, false, "1111");
        Assertions.assertThrows(UserTypeNotValidException.class, () -> unregisteredUserService.update(user, 1L));
    }

    @Test
    void delete_Valid_SavedObject() {
        UnregisteredUser deletedUser = unregisteredUserService.delete(4L);
        Assertions.assertNotNull(deletedUser);
    }

    @Test
    void delete_UserActive_ExceptionThrown() {
        Assertions.assertThrows(UnregisteredUserActiveException.class, () -> unregisteredUserService.delete(1L));
    }

    @Test
    void checkPinCode_Valid_ReturnedObject() {
        UnregisteredUser foundUser = unregisteredUserService.checkPinCode("1111", UserType.WAITER);
        Assertions.assertNotNull(foundUser);
    }

    @Test
    void checkPinCode_InvalidPinCode_ExceptionThrown() {
        Assertions.assertThrows(UserNotFoundException.class, () -> unregisteredUserService.checkPinCode("8989", UserType.WAITER));
    }

    @Test
    void checkPinCode_InvalidUserType_ExceptionThrown() {
        Assertions.assertThrows(UserNotFoundException.class, () -> unregisteredUserService.checkPinCode("8989", UserType.MANAGER));
    }
}