package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.dishitem.exception.DishItemNotFoundException;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.registereduser.exception.RegisteredUserDeleteException;
import akatsuki.restaurantsysteminformation.role.Role;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.user.UserService;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegisteredUserServiceTest {

    @InjectMocks
    RegisteredUserServiceImpl registeredUserService;

    @Mock
    RegisteredUserRepository registeredUserRepositoryMock;

    @Mock
    UserService userServiceMock;

    @Test
    public void getOne_ValidId_ReturnedObject() {
        RegisteredUser registeredUser = new RegisteredUser();

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(registeredUser));
        RegisteredUser foundRegisteredUser = registeredUserService.getOne(1L);

        Assertions.assertEquals(foundRegisteredUser, registeredUser);
    }

    @Test
    public void getOne_InvalidId_ExceptionThrown() {
        Mockito.when(registeredUserRepositoryMock.findById(8000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> registeredUserService.getOne(8000L));
    }

    @Test
    void getAll_RegisteredUsersExist_ReturnedList() {
        List<RegisteredUser> list = Collections.singletonList(new RegisteredUser());
        Mockito.when(registeredUserRepositoryMock.findAll()).thenReturn(list);

        List<RegisteredUser> foundList = registeredUserService.getAll();
        Assertions.assertEquals(foundList, list);
    }

    @Test
    void getAll_RegisteredUsersDontExist_ReturnedNull() {
        Mockito.when(registeredUserRepositoryMock.findAll()).thenReturn(null);
        List<RegisteredUser> foundList = registeredUserService.getAll();
        assertNull(foundList);
    }

    @Test
    void create_ValidEntity_SavedObject() {
        RegisteredUser user = new RegisteredUser(1L, "Michael", "Lock","michaellock@gmail.com",
                "0645678822", null, UserType.MANAGER, false, "michael", "lock", new Role());

        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        registeredUserService.create(user);

        Mockito.verify(userServiceMock, Mockito.times(1)).checkEmailExistence(user.getEmailAddress());
        Mockito.verify(userServiceMock, Mockito.times(1)).checkPhoneNumberExistence(user.getPhoneNumber());
        Mockito.verify(registeredUserRepositoryMock, Mockito.times(1)).save(user);
    }

    @Test
    void create_AlreadyExistUsername_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(2L, "Michael", "Lock","michaellock@gmail.com",
                "0645678822", null, UserType.MANAGER, false, "michael123", "lock", new Role());
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo","michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser));

        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.create(user));
    }

    @Test
    void create_InvalidUserType_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(2L, "Michael", "Lock","michaellock@gmail.com",
                "0645678822", null, UserType.ADMIN, false, "michael123", "lock", new Role());

        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserTypeNotValidException.class, () -> registeredUserService.create(user));

        Mockito.verify(userServiceMock, Mockito.times(1)).checkEmailExistence(user.getEmailAddress());
        Mockito.verify(userServiceMock, Mockito.times(1)).checkPhoneNumberExistence(user.getPhoneNumber());
    }

    @Test
    public void update_ValidEntityAndId_SavedObject() {
        RegisteredUser user = new RegisteredUser(2L, "Michael", "Lock","michaellock@gmail.com",
                "0645678822", null, UserType.MANAGER, false, "michael123", "lock", new Role());
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo","michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByEmail(user.getEmailAddress())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.empty());

        registeredUserService.update(user,1L);

        Assertions.assertEquals(existingUser.getFirstName(), user.getFirstName());
        Assertions.assertEquals(existingUser.getLastName(), user.getLastName());
        Assertions.assertEquals(existingUser.getEmailAddress(), user.getEmailAddress());
        Assertions.assertEquals(existingUser.getPhoneNumber(), user.getPhoneNumber());
        Assertions.assertEquals(existingUser.getPassword(), user.getPassword());
        Mockito.verify(registeredUserRepositoryMock, Mockito.times(1)).save(existingUser);
    }

    @Test
    public void update_ChangedUserType_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(2L, "Michael", "Lock","michaellock@gmail.com",
                "0645678822", null, UserType.MANAGER, false, "michael123", "lock", new Role());
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo","michaboo@gmail.com",
                "0645674444", null, UserType.SYSTEM_ADMIN, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));

        Assertions.assertThrows(UserTypeNotValidException.class, () -> registeredUserService.update(user,1L));
    }

    @Test
    public void update_UsernameAlreadyExist_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(2L, "Michael", "Lock","michaellock@gmail.com",
                "0645678822", null, UserType.MANAGER, false, "michael123", "lock", new Role());
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo","michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());
        RegisteredUser existingUser2 = new RegisteredUser(3L, "Micha", "Boo","michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser2));
        Mockito.when(userServiceMock.findByEmail(user.getEmailAddress())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.update(user,1L));
    }

    @Test
    public void update_EmailAlreadyExist_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(2L, "Michael", "Lock","michaellock@gmail.com",
                "0645678822", null, UserType.MANAGER, false, "michael123", "lock", new Role());
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo","michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());
        RegisteredUser existingUser2 = new RegisteredUser(3L, "Micha", "Boo","michaellock@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByEmail(user.getEmailAddress())).thenReturn(Optional.of(existingUser2));
        Mockito.when(userServiceMock.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.update(user,1L));
    }

    @Test
    public void update_PhoneNumberAlreadyExist_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(2L, "Michael", "Lock","michaellock@gmail.com",
                "0645678822", null, UserType.MANAGER, false, "michael123", "lock", new Role());
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo","michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());
        RegisteredUser existingUser2 = new RegisteredUser(3L, "Micha", "Boo","michaellock2@gmail.com",
                "0645678822", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByEmail(user.getEmailAddress())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.of(existingUser2));

        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.update(user,1L));
    }

    @Test
    public void delete_ValidId_SavedObject() {
        RegisteredUser user = new RegisteredUser(1L, "Micha", "Boo","michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
        registeredUserService.delete(1L);

        Assertions.assertTrue(user.isDeleted());
    }

    @Test
    public void delete_InvalidType_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(1L, "Micha", "Boo","michaboo@gmail.com",
                "0645674444", null, UserType.ADMIN, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));

        Assertions.assertThrows(RegisteredUserDeleteException.class, () -> registeredUserService.delete(1L));
    }
}