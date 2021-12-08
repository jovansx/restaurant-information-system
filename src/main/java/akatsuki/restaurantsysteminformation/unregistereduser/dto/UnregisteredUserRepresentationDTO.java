package akatsuki.restaurantsysteminformation.unregistereduser.dto;

import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UnregisteredUserRepresentationDTO extends UnregisteredUserDTO {
    private Long id;

    public UnregisteredUserRepresentationDTO(String pinCode, Long id) {
        super(pinCode);
        this.id = id;
    }

    public UnregisteredUserRepresentationDTO(UnregisteredUser user) {
        super(user.getFirstName(), user.getLastName(), user.getEmailAddress(), user.getPhoneNumber(), user.getSalary().get(user.getSalary().size() - 1).getValue(), user.getType(), user.getPinCode());
    }
}
