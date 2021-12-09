package akatsuki.restaurantsysteminformation.unregistereduser.dto;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.user.dto.UserDTO;
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
public class UnregisteredUserDTO extends UserDTO {
    @NotNull(message = "It cannot be null.")
    @Pattern(regexp = "[0-9]{4}", message = "It has to be 4 digits number.")
    private String pinCode;

    public UnregisteredUserDTO(UnregisteredUser user) {
        super(user.getFirstName(), user.getLastName(), user.getEmailAddress(),
                user.getPhoneNumber(), user.getSalary().get(user.getSalary().size() - 1).getValue(), user.getType());
        this.pinCode = user.getPinCode();
    }

    public UnregisteredUserDTO(@NotNull(message = "It cannot be null.") @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.") String firstName, @NotNull(message = "It cannot be null.") @Size(min = 3, max = 30, message = "It has to be between 3 and 30 characters long.") String lastName, @NotNull(message = "It cannot be null.") @Email(message = "Not valid email format.") String emailAddress, @NotNull(message = "It cannot be null.") @Pattern(regexp = "\\+[0-9]{12}|[0-9]{10}", message = "Phone number not match required format.") String phoneNumber, double salary, @NotNull(message = "It cannot be null.") UserType type, String pinCode) {
        super(firstName, lastName, emailAddress, phoneNumber, salary, type);
        this.pinCode = pinCode;
    }
}
