package akatsuki.restaurantsysteminformation.user.dto;

import akatsuki.restaurantsysteminformation.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserDTO {

    @NotNull(message = "It cannot be null.")
    @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.")
    private String firstName;

    @NotNull(message = "It cannot be null.")
    @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.")
    private String lastName;

    @NotNull(message = "It cannot be null.")
    @Email(message = "Not valid email format.")
    private String emailAddress;

    @NotNull(message = "It cannot be null.")
    @Pattern(regexp = "\\+[0-9]{12}|[0-9]{10}", message = "Phone number not match required format.")
    private String phoneNumber;

    private double salary;

    @NotNull(message = "It cannot be null.")
    private UserType type;
}
