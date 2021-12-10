package akatsuki.restaurantsysteminformation.user.dto;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserTableDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String type;

    public UserTableDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        configureType(user.getType());
    }

    public String getType() {
        return type;
    }

    public void configureType(UserType type) {
        this.type = type.name().toLowerCase();
    }
}
