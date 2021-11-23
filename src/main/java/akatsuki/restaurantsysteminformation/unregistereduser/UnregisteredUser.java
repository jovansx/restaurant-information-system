package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "unregistered_user")
@Getter
@Setter
@NoArgsConstructor
public class UnregisteredUser extends User {

    @Column(name = "pin_code", unique = true, nullable = false)
    private String pinCode;

    public UnregisteredUser(String firstName, String lastName, String emailAddress, String phoneNumber, List<Salary> salary, UserType type, boolean isDeleted, String pinCode) {
        super(firstName, lastName, emailAddress, phoneNumber, salary, type, isDeleted);
        this.pinCode = pinCode;
    }

    public UnregisteredUser(UnregisteredUserDTO unregisteredUserDTO) {
        super(unregisteredUserDTO.getFirstName(), unregisteredUserDTO.getLastName(), unregisteredUserDTO.getEmailAddress(),
                unregisteredUserDTO.getPhoneNumber(), Collections.singletonList(new Salary(LocalDateTime.now(), unregisteredUserDTO.getSalary())),
                unregisteredUserDTO.getType(), false);
        this.pinCode = unregisteredUserDTO.getPinCode();
    }
}
