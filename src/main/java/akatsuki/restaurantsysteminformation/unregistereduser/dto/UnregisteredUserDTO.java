package akatsuki.restaurantsysteminformation.unregistereduser.dto;

import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
}
