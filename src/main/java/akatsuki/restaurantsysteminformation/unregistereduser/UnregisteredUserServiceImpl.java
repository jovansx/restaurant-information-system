package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.dishitem.DishItemService;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItemsService;
import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.order.OrderService;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.salary.SalaryRepository;
import akatsuki.restaurantsysteminformation.salary.SalaryService;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserRepresentationDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.exception.UnregisteredUserActiveException;
import akatsuki.restaurantsysteminformation.user.User;
import akatsuki.restaurantsysteminformation.user.UserService;
import akatsuki.restaurantsysteminformation.user.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.user.exception.UserNotFoundException;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UnregisteredUserServiceImpl implements UnregisteredUserService {
    private UnregisteredUserRepository unregisteredUserRepository;
    private UserService userService;
    private OrderService orderService;
    private DrinkItemsService drinkItemsService;
    private DishItemService dishItemService;
    private SalaryService salaryService;

    @Autowired
    public void setUnregisteredUserRepository(UnregisteredUserRepository unregisteredUserRepository, UserService userService,
                                              OrderService orderService, DrinkItemsService drinkItemsService, @Lazy DishItemService dishItemService,
                                              @Lazy SalaryService salaryService) {
        this.unregisteredUserRepository = unregisteredUserRepository;
        this.userService = userService;
        this.drinkItemsService = drinkItemsService;
        this.dishItemService = dishItemService;
        this.orderService = orderService;
        this.salaryService = salaryService;
    }

    @Override
    public UnregisteredUser getOne(long id) {
        return unregisteredUserRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public List<UnregisteredUser> getAll() {
        return unregisteredUserRepository.findAll();
    }

    @Override
    public UnregisteredUser create(UnregisteredUserDTO unregisteredUserDTO) {
        checkPinCodeExistence(unregisteredUserDTO.getPinCode());
        userService.checkEmailExistence(unregisteredUserDTO.getEmailAddress());
        checkUserType(unregisteredUserDTO.getType());
        userService.checkPhoneNumberExistence(unregisteredUserDTO.getPhoneNumber());

        Salary salary = salaryService.create(new Salary(LocalDateTime.now(), unregisteredUserDTO.getSalary()));
        List<Salary> salaries = new ArrayList<>();
        salaries.add(salary);
        UnregisteredUser unregisteredUser = new UnregisteredUser(unregisteredUserDTO.getFirstName(),
                unregisteredUserDTO.getLastName(), unregisteredUserDTO.getEmailAddress(),
                unregisteredUserDTO.getPhoneNumber(), salaries,
                unregisteredUserDTO.getType(), false, unregisteredUserDTO.getPinCode());

        return unregisteredUserRepository.save(unregisteredUser);
    }

    @Override
    public UnregisteredUser update(UnregisteredUserDTO unregisteredUserDTO, long id) {
        UnregisteredUser user = getOne(id);
        validateUpdate(id, unregisteredUserDTO);

        user.setFirstName(unregisteredUserDTO.getFirstName());
        user.setLastName(unregisteredUserDTO.getLastName());
        user.setEmailAddress(unregisteredUserDTO.getEmailAddress());
        user.setPhoneNumber(unregisteredUserDTO.getPhoneNumber());

        if(unregisteredUserDTO.getSalary() != 0) {
            Salary salary = salaryService.create(new Salary(LocalDateTime.now(), unregisteredUserDTO.getSalary()));
            List<Salary> salaries = user.getSalary();
            salaries.add(salary);
            user.setSalary(salaries);
        }

        user.setPinCode(unregisteredUserDTO.getPinCode());

        return unregisteredUserRepository.save(user);
    }

    @Override
    public UnregisteredUser delete(long id) {
        UnregisteredUser user = getOne(id);
        if (!userCanBeDeleted(user))
            throw new UnregisteredUserActiveException("User with the id " + id + " is currently active and cannot be deleted now.");
        user.setDeleted(true);
        return unregisteredUserRepository.save(user);
    }

    @Override
    public void deleteById(long id) {
        unregisteredUserRepository.deleteById(id);
    }

    @Override
    public void save(UnregisteredUser unregisteredUser) {
        unregisteredUserRepository.save(unregisteredUser);
    }

    @Override
    public UnregisteredUser checkPinCode(String pinCode, UserType type) {
        UnregisteredUser user = unregisteredUserRepository.findByPinCode(pinCode)
                .orElseThrow(() -> new UserNotFoundException("User with the pin code " + pinCode + " is not found in the database."));
        if (!user.getType().equals(type))
            throw new UserNotFoundException("User with the pin code " + pinCode + " is not a " + type.name().toLowerCase());
        return user;
    }

    private void validateUpdate(long id, UnregisteredUserDTO unregisteredUserDTO) {
        checkUserType(unregisteredUserDTO.getType());
        Optional<UnregisteredUser> userByPinCode = unregisteredUserRepository.findByPinCode(unregisteredUserDTO.getPinCode());
        Optional<User> userByEmail = userService.findByEmail(unregisteredUserDTO.getEmailAddress());
        Optional<User> userByPhoneNumber = userService.findByPhoneNumber(unregisteredUserDTO.getPhoneNumber());

        if (userByPinCode.isPresent() && id != userByPinCode.get().getId())
            throw new UserExistsException("User with the pin code " + unregisteredUserDTO.getPinCode() + " already exists in the database.");

        if (userByEmail.isPresent() && id != userByEmail.get().getId())
            throw new UserExistsException("User with the email " + unregisteredUserDTO.getEmailAddress() + " already exists in the database.");

        if (userByPhoneNumber.isPresent() && id != userByPhoneNumber.get().getId())
            throw new UserExistsException("User with the phone number " + unregisteredUserDTO.getPhoneNumber() + " already exists in the database.");
    }

    private void checkPinCodeExistence(String pinCode) {
        Optional<UnregisteredUser> user = unregisteredUserRepository.findByPinCode(pinCode);
        if (user.isPresent())
            throw new UserExistsException("User with the pin code " + pinCode + " already exists in the database.");
    }

    private void checkUserType(UserType type) {
        if (type != UserType.WAITER && type != UserType.CHEF && type != UserType.BARTENDER)
            throw new UserTypeNotValidException("User type for unregistered user is not valid.");
    }

    private boolean userCanBeDeleted(UnregisteredUser user) {
        if (user.getType().equals(UserType.WAITER))
            return orderService.isWaiterActive(user);
        else if (user.getType().equals(UserType.BARTENDER))
            return drinkItemsService.isBartenderActive(user);
        else if (user.getType().equals(UserType.CHEF))
            return dishItemService.isChefActive(user);
        return false;
    }
}
