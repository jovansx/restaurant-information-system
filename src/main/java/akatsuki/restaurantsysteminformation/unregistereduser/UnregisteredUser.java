package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UnregisteredUser extends User {

    @Column(name = "pin_code", unique = true, nullable = false)
    private String pinCode;

    public UnregisteredUser() {
    }

    public UnregisteredUser(String firstName, String lastName, String emailAddress, String phoneNumber, double salary, UserType type, boolean isDeleted, String pinCode) {
        super(firstName, lastName, emailAddress, phoneNumber, salary, type, isDeleted);
        this.pinCode = pinCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
