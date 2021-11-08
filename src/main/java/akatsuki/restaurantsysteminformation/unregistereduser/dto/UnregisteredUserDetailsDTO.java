package akatsuki.restaurantsysteminformation.unregistereduser.dto;

import akatsuki.restaurantsysteminformation.user.dto.UserDTO;

public class UnregisteredUserDetailsDTO extends UserDTO {

    private String pinCode;

    public UnregisteredUserDetailsDTO() {}

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
