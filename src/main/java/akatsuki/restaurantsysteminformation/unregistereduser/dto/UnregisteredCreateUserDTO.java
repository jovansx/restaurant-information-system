package akatsuki.restaurantsysteminformation.unregistereduser.dto;

import akatsuki.restaurantsysteminformation.user.dto.CreateUserDTO;

public class UnregisteredCreateUserDTO extends CreateUserDTO {
    private String pinCode;

    public UnregisteredCreateUserDTO(String firstName, String lastName, String emailAddress, String phoneNumber, double salary, String type,
                                     String pinCode) {
        super(firstName, lastName, emailAddress, phoneNumber, salary, type);
        this.pinCode = pinCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
