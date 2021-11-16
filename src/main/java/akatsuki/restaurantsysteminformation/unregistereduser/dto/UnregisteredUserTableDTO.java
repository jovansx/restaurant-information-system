package akatsuki.restaurantsysteminformation.unregistereduser.dto;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UnregisteredUserTableDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String type;

    public UnregisteredUserTableDTO(UnregisteredUser user) {
        this.id = user.getId();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        setType(user.getType());
    }

    public String getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type.name().toLowerCase();
    }
}
