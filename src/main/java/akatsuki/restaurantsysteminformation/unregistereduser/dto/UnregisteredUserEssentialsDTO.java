package akatsuki.restaurantsysteminformation.unregistereduser.dto;

import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UnregisteredUserEssentialsDTO {
    private long id;
    private String pinCode;

    public UnregisteredUserEssentialsDTO(UnregisteredUser user) {
        this.id = user.getId();
        this.pinCode = user.getPinCode();
    }
}
