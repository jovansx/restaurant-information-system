package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserChangePasswordDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDetailsDTO;
import akatsuki.restaurantsysteminformation.registereduser.exception.RegisteredUserDeleteException;
import akatsuki.restaurantsysteminformation.registereduser.exception.RegisteredUserPasswordException;
import akatsuki.restaurantsysteminformation.role.Role;
import akatsuki.restaurantsysteminformation.role.RoleRepository;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.salary.SalaryService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RegisteredUserServiceTest {

    @InjectMocks
    RegisteredUserServiceImpl registeredUserService;

    @Mock
    RegisteredUserRepository registeredUserRepositoryMock;

    @Mock
    UserService userServiceMock;

    @Mock
    SalaryService salaryServiceMock;

    @Mock
    RoleRepository roleRepositoryMock;

    @Mock
    PasswordEncoder passwordEncoderMock;

    @Test
    public void getOne_ValidId_ReturnedObject() {
        RegisteredUser registeredUser = new RegisteredUser();

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(registeredUser));
        RegisteredUser foundRegisteredUser = registeredUserService.getOne(1L);

        Assertions.assertEquals(registeredUser, foundRegisteredUser);
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
        Assertions.assertEquals(list, foundList);
    }

    @Test
    void getAll_RegisteredUsersDontExist_ReturnedEmpty() {
        Mockito.when(registeredUserRepositoryMock.findAll()).thenReturn(new ArrayList<>());
        List<RegisteredUser> foundList = registeredUserService.getAll();
        assertEquals(0, foundList.size());
    }

    @Test
    void getAllSystemAdmins_SystemAdminsExist_ReturnedList() {
        RegisteredUser user = new RegisteredUser();
        user.setType(UserType.SYSTEM_ADMIN);
        List<RegisteredUser> list = Collections.singletonList(user);
        Mockito.when(registeredUserRepositoryMock.findAllSystemAdmins()).thenReturn(list);

        List<RegisteredUser> foundList = registeredUserService.getAllSystemAdmins();
        Assertions.assertEquals(list, foundList);
    }

    @Test
    void create_ValidEntity_SavedObject() {
        RegisteredUserDTO userDTO = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.MANAGER, "michael", "lock");
        Mockito.when(registeredUserRepositoryMock.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(roleRepositoryMock.findByName("MANAGER")).thenReturn(Optional.of(new Role()));
        registeredUserService.create(userDTO);

        Mockito.verify(userServiceMock, Mockito.times(1)).checkEmailExistence(userDTO.getEmailAddress());
        Mockito.verify(userServiceMock, Mockito.times(1)).checkPhoneNumberExistence(userDTO.getPhoneNumber());
        Mockito.verify(registeredUserRepositoryMock, Mockito.times(1)).save(Mockito.any(RegisteredUser.class));
    }

    @Test
    void create_AlreadyExistUsername_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.MANAGER, "michael123", "lock");
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo", "michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser));

        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.create(user));
    }

    @Test
    void create_InvalidUserType_ExceptionThrown() {
        RegisteredUserDTO user = new RegisteredUserDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.ADMIN, "michael123", "lock");

        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserTypeNotValidException.class, () -> registeredUserService.create(user));

        Mockito.verify(userServiceMock, Mockito.times(1)).checkEmailExistence(user.getEmailAddress());
        Mockito.verify(userServiceMock, Mockito.times(1)).checkPhoneNumberExistence(user.getPhoneNumber());
    }

    @Test
    public void update_ValidEntityAndId_SavedObject() {
        RegisteredUserDetailsDTO user = new RegisteredUserDetailsDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.MANAGER, "michael123");
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo", "michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByEmail(user.getEmailAddress())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.empty());

        registeredUserService.update(user, 1L);

        Assertions.assertEquals(existingUser.getFirstName(), user.getFirstName());
        Assertions.assertEquals(existingUser.getLastName(), user.getLastName());
        Assertions.assertEquals(existingUser.getEmailAddress(), user.getEmailAddress());
        Assertions.assertEquals(existingUser.getPhoneNumber(), user.getPhoneNumber());
        Mockito.verify(registeredUserRepositoryMock, Mockito.times(1)).save(existingUser);
    }

    @Test
    public void update_ValidEntityAndIdWithSalary_SavedObject() {
        RegisteredUserDetailsDTO user = new RegisteredUserDetailsDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 1000, UserType.MANAGER, "michael123");
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo", "michaboo@gmail.com",
                "0645674444", new ArrayList<>(), UserType.MANAGER, false, "michael123", "boo", new Role());
        Salary salary = new Salary(LocalDateTime.now(), 1000);

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByEmail(user.getEmailAddress())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.empty());
        Mockito.when(salaryServiceMock.create(Mockito.any(Salary.class))).thenReturn(salary);

        registeredUserService.update(user, 1L);

        Assertions.assertEquals(existingUser.getFirstName(), user.getFirstName());
        Assertions.assertEquals(existingUser.getLastName(), user.getLastName());
        Assertions.assertEquals(existingUser.getEmailAddress(), user.getEmailAddress());
        Assertions.assertEquals(existingUser.getPhoneNumber(), user.getPhoneNumber());
        Assertions.assertEquals(salary, existingUser.getSalary().get(0));
        Mockito.verify(registeredUserRepositoryMock, Mockito.times(1)).save(existingUser);
    }

    @Test
    public void update_ChangedUserType_ExceptionThrown() {
        RegisteredUserDetailsDTO user = new RegisteredUserDetailsDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.MANAGER, "michael123");
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo", "michaboo@gmail.com",
                "0645674444", null, UserType.SYSTEM_ADMIN, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));

        Assertions.assertThrows(UserTypeNotValidException.class, () -> registeredUserService.update(user, 1L));
    }

    @Test
    public void update_UsernameAlreadyExist_ExceptionThrown() {
        RegisteredUserDetailsDTO user = new RegisteredUserDetailsDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.MANAGER, "michael123");
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo", "michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());
        RegisteredUser existingUser2 = new RegisteredUser(3L, "Micha", "Boo", "michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser2));
        Mockito.when(userServiceMock.findByEmail(user.getEmailAddress())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.update(user, 1L));
    }

    @Test
    public void update_EmailAlreadyExist_ExceptionThrown() {
        RegisteredUserDetailsDTO user = new RegisteredUserDetailsDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.MANAGER, "michael123");
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo", "michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());
        RegisteredUser existingUser2 = new RegisteredUser(3L, "Micha", "Boo", "michaellock@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByEmail(user.getEmailAddress())).thenReturn(Optional.of(existingUser2));
        Mockito.when(userServiceMock.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.update(user, 1L));
    }

    @Test
    public void update_PhoneNumberAlreadyExist_ExceptionThrown() {
        RegisteredUserDetailsDTO user = new RegisteredUserDetailsDTO("Michael", "Lock", "michaellock@gmail.com",
                "0645678822", 0, UserType.MANAGER, "michael123");
        RegisteredUser existingUser = new RegisteredUser(1L, "Micha", "Boo", "michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());
        RegisteredUser existingUser2 = new RegisteredUser(3L, "Micha", "Boo", "michaellock2@gmail.com",
                "0645678822", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(registeredUserRepositoryMock.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByEmail(user.getEmailAddress())).thenReturn(Optional.of(existingUser));
        Mockito.when(userServiceMock.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.of(existingUser2));

        Assertions.assertThrows(UserExistsException.class, () -> registeredUserService.update(user, 1L));
    }

    @Test
    public void changePassword_ValidDTO_SavedObject() {
        RegisteredUserChangePasswordDTO dto = new RegisteredUserChangePasswordDTO("liamneeson", "liamneesonstronger");
        RegisteredUser user = new RegisteredUser(1L, "Liam", "Neeson", "liamneeson@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "liamneeson", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoderMock.encode(dto.getNewPassword())).thenReturn("some hash value");
        Mockito.when(passwordEncoderMock.matches(dto.getOldPassword(), user.getPassword())).thenReturn(true);

        RegisteredUser changedUser = registeredUserService.changePassword(dto, 1);

        assertEquals("some hash value", changedUser.getPassword());
        Mockito.verify(registeredUserRepositoryMock, Mockito.times(1)).save(user);
    }

    @Test
    public void changePassword_PasswordsNotMatches_ValidDTO_SavedObject() {
        RegisteredUserChangePasswordDTO dto = new RegisteredUserChangePasswordDTO("liamneeson2", "liamneesonstronger");
        RegisteredUser user = new RegisteredUser(1L, "Liam", "Neeson", "liamneeson@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "liamneeson", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoderMock.encode(dto.getNewPassword())).thenReturn("some hash value");
        Mockito.when(passwordEncoderMock.matches(dto.getOldPassword(), user.getPassword())).thenReturn(false);

        Assertions.assertThrows(RegisteredUserPasswordException.class, () -> registeredUserService.changePassword(dto, 1L));
    }

    @Test
    public void delete_ValidId_SavedObject() {
        RegisteredUser user = new RegisteredUser(1L, "Micha", "Boo", "michaboo@gmail.com",
                "0645674444", null, UserType.MANAGER, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
        registeredUserService.delete(1L);

        Assertions.assertTrue(user.isDeleted());
    }

    @Test
    public void delete_InvalidType_ExceptionThrown() {
        RegisteredUser user = new RegisteredUser(1L, "Micha", "Boo", "michaboo@gmail.com",
                "0645674444", null, UserType.ADMIN, false, "michael123", "boo", new Role());

        Mockito.when(registeredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));

        Assertions.assertThrows(RegisteredUserDeleteException.class, () -> registeredUserService.delete(1L));
    }
}