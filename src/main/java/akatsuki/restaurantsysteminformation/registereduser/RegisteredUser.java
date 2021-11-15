package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

    public RegisteredUser(String firstName, String lastName, String emailAddress, String phoneNumber, double salary, UserType type, boolean isDeleted, String username, String password) {
        super(firstName, lastName, emailAddress, phoneNumber, salary, type, isDeleted);
        this.username = username;
        this.password = password;
    }

    public RegisteredUser(RegisteredUserDTO registeredUserDTO) {
        super(registeredUserDTO.getFirstName(), registeredUserDTO.getLastName(), registeredUserDTO.getEmailAddress(),
                registeredUserDTO.getPhoneNumber(), registeredUserDTO.getSalary(), registeredUserDTO.getType(), false);
        this.username = registeredUserDTO.getUsername();
        this.password = registeredUserDTO.getPassword();
    }
}
