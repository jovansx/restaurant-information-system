package akatsuki.restaurantsysteminformation.registereduser.dto;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.RegisteredUser;
import akatsuki.restaurantsysteminformation.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredUserDetailsDTO extends UserDTO {
    private String username;

    public RegisteredUserDetailsDTO(String firstName, String lastName, String emailAddress, String phoneNumber, double salary, UserType type, String username) {
        super(firstName, lastName, emailAddress, phoneNumber, salary, type);
        this.username = username;
    }

    public RegisteredUserDetailsDTO(RegisteredUser user) {
        this.username = user.getUsername();
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setEmailAddress(user.getEmailAddress());
        setPhoneNumber(user.getPhoneNumber());
        setSalary(user.getSalary().get(user.getSalary().size() - 1).getValue());
        setType(user.getType());
    }
}
