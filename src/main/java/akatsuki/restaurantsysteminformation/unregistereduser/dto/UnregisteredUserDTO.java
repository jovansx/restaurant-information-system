package akatsuki.restaurantsysteminformation.unregistereduser.dto;

import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.user.dto.UserDTO;

public class UnregisteredUserDTO extends UserDTO {
    private String pinCode;

    public UnregisteredUserDTO() {
    }

    public UnregisteredUserDTO(String firstName, String lastName, String emailAddress, String phoneNumber, double salary, String type,
                               String pinCode) {
        super(firstName, lastName, emailAddress, phoneNumber, salary, type);
        this.pinCode = pinCode;
    }

    public UnregisteredUserDTO(UnregisteredUser user) {
        super(user.getFirstName(), user.getLastName(), user.getEmailAddress(),
                user.getPhoneNumber(), user.getSalary(), user.getType().toString().toLowerCase());
        this.pinCode = user.getPinCode();
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
