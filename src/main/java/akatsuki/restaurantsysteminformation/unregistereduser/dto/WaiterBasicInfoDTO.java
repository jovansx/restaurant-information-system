package akatsuki.restaurantsysteminformation.unregistereduser.dto;

import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WaiterBasicInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;

    public WaiterBasicInfoDTO(UnregisteredUser waiter) {
        this.id = waiter.getId();
        this.firstName = waiter.getFirstName();
        this.lastName = waiter.getLastName();
    }
}
