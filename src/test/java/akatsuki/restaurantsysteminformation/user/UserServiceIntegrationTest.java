package akatsuki.restaurantsysteminformation.user;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceIntegrationTest {

    @Autowired
    UserServiceImpl userService;

    @Test
    void findByEmail_ValidEmail_ObjectReturned() {
        Optional<User> userOptional = userService.findByEmail("johncena@gmail.com");
        Assertions.assertTrue(userOptional.isPresent());
    }

    @Test
    void findByEmail_InvalidEmail_ReturnedOptionalEmpty() {
        Optional<User> userOptional = userService.findByEmail("aca@gmail.com");
        Assertions.assertTrue(userOptional.isEmpty());
    }

    @Test
    void findByPhoneNumber_ValidPhoneNumber_ObjectReturned() {
        Optional<User> userOptional = userService.findByPhoneNumber("0611111111");
        Assertions.assertTrue(userOptional.isPresent());
    }

    @Test
    void findByPhoneNumber_InvalidPhoneNumber_ReturnedOptionalEmpty() {
        Optional<User> userOptional = userService.findByPhoneNumber("0000000000");
        Assertions.assertTrue(userOptional.isEmpty());
    }

    @Test
    void checkEmailExistence_InvalidEmail_ExceptionThrown() {
        Assertions.assertThrows(UserExistsException.class, () -> userService.checkEmailExistence("johncena@gmail.com"));
    }

    @Test
    void checkPhoneNumberExistence_InvalidPhoneNumber_ExceptionThrown() {
        Assertions.assertThrows(UserExistsException.class, () -> userService.checkPhoneNumberExistence("0611111111"));
    }

    @Test
    void getAllManagersAndUnregistered_Valid_ReturnedList() {
        List<User> users = userService.getAllManagersAndUnregistered();
        for (User user : users) {
            Assertions.assertTrue(UserType.MANAGER.equals(user.getType()) ||
                    UserType.WAITER.equals(user.getType()) || UserType.CHEF.equals(user.getType()) || UserType.BARTENDER.equals(user.getType()));
        }
    }
}