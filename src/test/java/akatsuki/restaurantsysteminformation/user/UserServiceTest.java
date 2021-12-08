package akatsuki.restaurantsysteminformation.user;

import akatsuki.restaurantsysteminformation.registereduser.RegisteredUser;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepositoryMock;

    @Test
    void checkEmailExistence_InvalidEmail_ExceptionThrown() {
        User user = new RegisteredUser();
        user.setEmailAddress("mika@gmail.com");
        Mockito.when(userRepositoryMock.findByEmailAddress("mika@gmail.com")).thenReturn(Optional.of(user));
        Assertions.assertThrows(UserExistsException.class, () -> userService.checkEmailExistence("mika@gmail.com"));
    }

    @Test
    void checkPhoneNumberExistence_InvalidPhoneNumber_ExceptionThrown() {
        User user = new RegisteredUser();
        user.setPhoneNumber("069112233");
        Mockito.when(userRepositoryMock.findByPhoneNumber("069112233")).thenReturn(Optional.of(user));
        Assertions.assertThrows(UserExistsException.class, () -> userService.checkPhoneNumberExistence("069112233"));
    }
}