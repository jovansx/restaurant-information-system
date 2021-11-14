package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class RegisteredUser extends User {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public RegisteredUser() {
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
