package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDetailsDTO;
import akatsuki.restaurantsysteminformation.role.Role;
import akatsuki.restaurantsysteminformation.salary.Salary;
import akatsuki.restaurantsysteminformation.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "registered_user")
@Getter
@Setter
@NoArgsConstructor
public class RegisteredUser extends User {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Role role;

    public RegisteredUser(Long id, String firstName, String lastName, String emailAddress, String phoneNumber, List<Salary> salary, UserType type, boolean isDeleted, String username, String password, Role role) {
        super(id, firstName, lastName, emailAddress, phoneNumber, salary, type, isDeleted);
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public RegisteredUser(String firstName, String lastName, String emailAddress, String phoneNumber,
                          List<Salary> salary, UserType type, boolean isDeleted, String username, String password, Role role) {
        super(firstName, lastName, emailAddress, phoneNumber, salary, type, isDeleted);
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public RegisteredUser(RegisteredUserDTO registeredUserDTO) {
        super(registeredUserDTO.getFirstName(), registeredUserDTO.getLastName(), registeredUserDTO.getEmailAddress(),
                registeredUserDTO.getPhoneNumber(), Collections.singletonList(new Salary(LocalDateTime.now(), registeredUserDTO.getSalary())), registeredUserDTO.getType(), false);
        this.username = registeredUserDTO.getUsername();
        this.password = registeredUserDTO.getPassword();
    }

    public RegisteredUser(RegisteredUserDetailsDTO registeredUserDTO) {
        super(registeredUserDTO.getFirstName(), registeredUserDTO.getLastName(), registeredUserDTO.getEmailAddress(),
                registeredUserDTO.getPhoneNumber(), Collections.singletonList(new Salary(LocalDateTime.now(), registeredUserDTO.getSalary())), registeredUserDTO.getType(), false);
        this.username = registeredUserDTO.getUsername();
    }
}
