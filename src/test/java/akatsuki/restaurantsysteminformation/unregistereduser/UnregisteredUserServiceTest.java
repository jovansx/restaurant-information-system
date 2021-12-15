package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.dishitem.DishItemService;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItemsService;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.salary.SalaryService;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.exception.UnregisteredUserActiveException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UnregisteredUserServiceTest {

    @InjectMocks
    UnregisteredUserServiceImpl unregisteredUserService;

    @Mock
    UnregisteredUserRepository unregisteredUserRepositoryMock;

    @Mock
    UserService userServiceMock;

    @Mock
    private OrderService orderServiceMock;

    @Mock
    private DrinkItemsService drinkItemsServiceMock;

    @Mock
    private DishItemService dishItemServiceMock;

    @Mock
    private SalaryService salaryServiceMock;

    @Test
    void getOne_Valid_ReturnedObject() {
        Mockito.when(unregisteredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(new UnregisteredUser()));
        UnregisteredUser user = unregisteredUserService.getOne(1);
        Assertions.assertNotNull(user);
    }

    @Test
    void getOne_InvalidId_ExceptionThrown() {
        Mockito.when(unregisteredUserRepositoryMock.findById(400L)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> unregisteredUserService.getOne(400L));
    }


    @Test
    void create_Valid_SavedObject() {
        UnregisteredUserDTO userDTO = new UnregisteredUserDTO("Jelena", "Stojanovic", "sekica@gmail.com", "069111223", 1000000, UserType.WAITER, "9999");
        unregisteredUserService.create(userDTO);
        Mockito.verify(unregisteredUserRepositoryMock, Mockito.times(1)).save(Mockito.any(UnregisteredUser.class));
    }

    @Test
    void create_InvalidPin_ExceptionThrown() {
        UnregisteredUserDTO userDTO = new UnregisteredUserDTO("Jelena", "Stojanovic", "sekica@gmail.com", "069111223", 1000000, UserType.WAITER, "1111");
        Mockito.when(unregisteredUserRepositoryMock.findByPinCode("1111")).thenReturn(Optional.of(new UnregisteredUser()));
        Assertions.assertThrows(UserExistsException.class, () -> unregisteredUserService.create(userDTO));
    }

    @Test
    void create_InvalidUserType_ExceptionThrown() {
        UnregisteredUserDTO userDTO = new UnregisteredUserDTO("Jelena", "Stojanovic", "sekica@gmail.com", "069111223", 1000000, UserType.MANAGER, "1111");
        Assertions.assertThrows(UserTypeNotValidException.class, () -> unregisteredUserService.create(userDTO));
    }

    @Test
    void update_Valid_SavedObject() {
        UnregisteredUserDTO userDTO = new UnregisteredUserDTO("Milan", "Stankovic", "miki@gmail.com", "069111223", 1000, UserType.WAITER, "9999");
        UnregisteredUser user = new UnregisteredUser();
        user.setSalary(new ArrayList<>());
        Mockito.when(unregisteredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
        unregisteredUserService.update(userDTO, 1L);
        Mockito.verify(unregisteredUserRepositoryMock, Mockito.times(1)).save(Mockito.any(UnregisteredUser.class));
    }

    @Test
    void update_InvalidPin_ExceptionThrown() {
        UnregisteredUserDTO userDTO = new UnregisteredUserDTO("Milan", "Stankovic", "miki@gmail.com", "069111223", 1000, UserType.WAITER, "1111");
        UnregisteredUser user = new UnregisteredUser();
        user.setId(1L);
        Mockito.when(unregisteredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
        UnregisteredUser un = new UnregisteredUser();
        un.setId(2L);
        Mockito.when(unregisteredUserRepositoryMock.findByPinCode("1111")).thenReturn(Optional.of(un));
        Assertions.assertThrows(UserExistsException.class, () -> unregisteredUserService.update(userDTO, 1L));
    }

    @Test
    void update_InvalidEmail_ExceptionThrown() {
        UnregisteredUserDTO userDTO = new UnregisteredUserDTO("Milan", "Stankovic", "miki@gmail.com", "069111223", 1000, UserType.WAITER, "1111");
        UnregisteredUser user = new UnregisteredUser();
        user.setId(1L);
        UnregisteredUser un = new UnregisteredUser();
        un.setId(1L);
        UnregisteredUser un2 = new UnregisteredUser();
        un2.setId(2L);
        Mockito.when(unregisteredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(unregisteredUserRepositoryMock.findByPinCode("1111")).thenReturn(Optional.of(un));
        Mockito.when(userServiceMock.findByEmail(Mockito.any())).thenReturn(Optional.of(un2));

        Assertions.assertThrows(UserExistsException.class, () -> unregisteredUserService.update(userDTO, 1L));
    }

    @Test
    void update_InvalidPhoneNumber_ExceptionThrown() {
        UnregisteredUserDTO userDTO = new UnregisteredUserDTO("Milan", "Stankovic", "miki@gmail.com", "069111223", 1000, UserType.WAITER, "1111");
        UnregisteredUser user = new UnregisteredUser();
        user.setId(1L);
        UnregisteredUser un = new UnregisteredUser();
        un.setId(1L);
        UnregisteredUser un2 = new UnregisteredUser();
        un2.setId(1L);
        UnregisteredUser un3 = new UnregisteredUser();
        un3.setId(2L);

        Mockito.when(unregisteredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(unregisteredUserRepositoryMock.findByPinCode("1111")).thenReturn(Optional.of(un));
        Mockito.when(userServiceMock.findByEmail(Mockito.any())).thenReturn(Optional.of(un2));
        Mockito.when(userServiceMock.findByPhoneNumber(Mockito.any())).thenReturn(Optional.of(un3));

        Assertions.assertThrows(UserExistsException.class, () -> unregisteredUserService.update(userDTO, 1L));
    }

    @Test
    void update_InvalidUserType_ExceptionThrown() {
        UnregisteredUserDTO user = new UnregisteredUserDTO("Jelena", "Stojanovic", "sekica@gmail.com", "069111223", 12, UserType.MANAGER, "1111");
        Mockito.when(unregisteredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(new UnregisteredUser()));
        Assertions.assertThrows(UserTypeNotValidException.class, () -> unregisteredUserService.update(user, 1L));
    }

    @Test
    void delete_Valid_SavedObject() {
        UnregisteredUser user = new UnregisteredUser("Milan", "Petrovic", "sekica@gmail.com", "069111223", Collections.singletonList(new Salary(LocalDateTime.now(), 1000000)), UserType.WAITER, false, "1111");
        user.setType(UserType.WAITER);
        Mockito.when(unregisteredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(orderServiceMock.isWaiterActive(user)).thenReturn(true);
        unregisteredUserService.delete(1L);
        Mockito.verify(unregisteredUserRepositoryMock, Mockito.times(1)).save(Mockito.any(UnregisteredUser.class));
    }

    @Test
    void delete_UserCannotBeDeleted_ExceptionThrown() {
        UnregisteredUser user = new UnregisteredUser("Milan", "Petrovic", "sekica@gmail.com", "069111223", Collections.singletonList(new Salary(LocalDateTime.now(), 1000000)), UserType.WAITER, false, "1111");
        user.setType(UserType.WAITER);
        Mockito.when(unregisteredUserRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(orderServiceMock.isWaiterActive(user)).thenReturn(false);
        Assertions.assertThrows(UnregisteredUserActiveException.class, () -> unregisteredUserService.delete(1L));
    }

    @Test
    void checkPinCode_Valid_ReturnedObject() {
        UnregisteredUser user = new UnregisteredUser("Milan", "Petrovic", "sekica@gmail.com", "069111223", Collections.singletonList(new Salary(LocalDateTime.now(), 1000000)), UserType.WAITER, false, "1111");
        Mockito.when(unregisteredUserRepositoryMock.findByPinCode("1111")).thenReturn(Optional.of(user));
        UnregisteredUser foundUser = unregisteredUserService.checkPinCode("1111", UserType.WAITER);
        Assertions.assertEquals(foundUser, user);
    }

    @Test
    void checkPinCode_InvalidPinCode_ExceptionThrown() {
        Mockito.when(unregisteredUserRepositoryMock.findByPinCode("8989")).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> unregisteredUserService.checkPinCode("8989", UserType.WAITER));
    }

    @Test
    void checkPinCode_InvalidUserType_ExceptionThrown() {
        UnregisteredUser user = new UnregisteredUser();
        user.setType(UserType.CHEF);
        Mockito.when(unregisteredUserRepositoryMock.findByPinCode("1112")).thenReturn(Optional.of(user));
        Assertions.assertThrows(UserNotFoundException.class, () -> unregisteredUserService.checkPinCode("1112", UserType.MANAGER));
    }
}