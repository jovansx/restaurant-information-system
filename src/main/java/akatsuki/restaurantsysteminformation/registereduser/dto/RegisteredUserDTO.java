package akatsuki.restaurantsysteminformation.registereduser.dto;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.RegisteredUser;
import akatsuki.restaurantsysteminformation.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredUserDTO extends UserDTO {

    @NotNull(message = "It cannot be null.")
    @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.")
    private String username;

    @NotNull(message = "It cannot be null.")
    @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.")
    private String password;

    public RegisteredUserDTO(@NotNull(message = "It cannot be null.") @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.") String firstName, @NotNull(message = "It cannot be null.") @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.") String lastName, @NotNull(message = "It cannot be null.") @Email(message = "Not valid email format.") String emailAddress, @NotNull(message = "It cannot be null.") @Pattern(regexp = "\\+[0-9]{12}|[0-9]{10}", message = "Phone number not match required format.") String phoneNumber, @NotNull(message = "It cannot be null.") @Positive(message = "It has to be a positive number.") double salary, @NotNull(message = "It cannot be null.") UserType type, String username, String password) {
        super(firstName, lastName, emailAddress, phoneNumber, salary, type);
        this.username = username;
        this.password = password;
    }

    public RegisteredUserDTO(RegisteredUser user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setEmailAddress(user.getEmailAddress());
        setPhoneNumber(user.getPhoneNumber());
        setSalary(user.getSalary().get(user.getSalary().size() - 1).getValue());
        setType(user.getType());
    }
}
