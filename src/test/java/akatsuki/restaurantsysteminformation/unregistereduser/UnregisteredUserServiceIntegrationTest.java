package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.exception.UnregisteredUserActiveException;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UnregisteredUserServiceIntegrationTest {

    @Autowired
    UnregisteredUserServiceImpl unregisteredUserService;

    @Test
    public void getOne_ValidId_ReturnedObject() {
        UnregisteredUser foundRegisteredUser = unregisteredUserService.getOne(1L);
        Assertions.assertNotNull(foundRegisteredUser);
    }

    @Test
    public void getOne_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(UserNotFoundException.class, () -> unregisteredUserService.getOne(8000L));
    }

    @Test
    public void getAll_Valid_ReturnedList() {
        List<UnregisteredUser> list = unregisteredUserService.getAll();
        Assertions.assertEquals(8, list.size());
    }


    @Test
    void create_Valid_SavedObject() {
        UnregisteredUserDTO user = new UnregisteredUserDTO("Jelena", "Stojanovic", "sekica@gmail.com", "069121223", 12, UserType.WAITER, "9999");
        UnregisteredUser createdUser = unregisteredUserService.create(user);
        Assertions.assertNotNull(createdUser);
    }

    @Test
    void create_InvalidPin_ExceptionThrown() {
        UnregisteredUserDTO user = new UnregisteredUserDTO("Jelena", "Stojanovic", "sekica@gmail.com", "069111223", 12, UserType.WAITER, "1111");
        Assertions.assertThrows(UserExistsException.class, () -> unregisteredUserService.create(user));
    }

    @Test
    void create_InvalidUserType_ExceptionThrown() {
        UnregisteredUserDTO user = new UnregisteredUserDTO("Jelena", "Stojanovic", "sekica@gmail.com", "069111223", 12, UserType.MANAGER, "8888");
        Assertions.assertThrows(UserTypeNotValidException.class, () -> unregisteredUserService.create(user));
    }

    @Test
    void update_Valid_SavedObject() {
        UnregisteredUserDTO user = new UnregisteredUserDTO("Simon", "Baker", "simonbaker@gmail.com", "0611111112", 145, UserType.WAITER, "1112");
        UnregisteredUser updatedUser = unregisteredUserService.update(user, 2L);
        Assertions.assertNotNull(updatedUser);
    }

    @Test
    void update_InvalidPin_ExceptionThrown() {
        UnregisteredUserDTO user = new UnregisteredUserDTO("John", "Cena", "johncena@gmail.com", "069111223", 13, UserType.WAITER, "1112");
        Assertions.assertThrows(UserExistsException.class, () -> unregisteredUserService.update(user, 1L));
    }

    @Test
    void update_InvalidEmail_ExceptionThrown() {
        UnregisteredUserDTO user = new UnregisteredUserDTO("John", "Cena", "simonbaker@gmail.com", "069111223", 13, UserType.WAITER, "1111");
        Assertions.assertThrows(UserExistsException.class, () -> unregisteredUserService.update(user, 1L));
    }

    @Test
    void update_InvalidPhoneNumber_ExceptionThrown() {
        UnregisteredUserDTO user = new UnregisteredUserDTO("John", "Cena", "johncena@gmail.com", "0611111113", 13, UserType.WAITER, "1111");
        Assertions.assertThrows(UserExistsException.class, () -> unregisteredUserService.update(user, 1L));
    }

    @Test
    void update_InvalidUserType_ExceptionThrown() {
        UnregisteredUserDTO user = new UnregisteredUserDTO("John", "Cena", "johncena@gmail.com", "069111223", 13, UserType.MANAGER, "1111");
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
        Assertions.assertThrows(UserNotFoundException.class, () -> unregisteredUserService.checkPinCode("1112", UserType.MANAGER));
    }
}