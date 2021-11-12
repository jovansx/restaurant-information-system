package akatsuki.restaurantsysteminformation.user.dto;

import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

public class WaiterBasicInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;

    public WaiterBasicInfoDTO() {
    }

    public WaiterBasicInfoDTO(UnregisteredUser waiter) {
        this.id = waiter.getId();
        this.firstName = waiter.getFirstName();
        this.lastName = waiter.getLastName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
