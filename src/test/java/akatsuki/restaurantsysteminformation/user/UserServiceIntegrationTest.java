package akatsuki.restaurantsysteminformation.user;

import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceIntegrationTest {

    @Autowired
    UserServiceImpl userService;

    @Test
    void checkEmailExistence_InvalidEmail_ExceptionThrown() {
        Assertions.assertThrows(UserExistsException.class, () -> userService.checkEmailExistence("johncena@gmail.com"));
    }

    @Test
    void checkPhoneNumberExistence_InvalidPhoneNumber_ExceptionThrown() {
        Assertions.assertThrows(UserExistsException.class, () -> userService.checkPhoneNumberExistence("0611111111"));
    }
}