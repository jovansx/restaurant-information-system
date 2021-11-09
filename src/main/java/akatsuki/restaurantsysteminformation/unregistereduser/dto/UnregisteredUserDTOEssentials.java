package akatsuki.restaurantsysteminformation.unregistereduser.dto;

import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;

public class UnregisteredUserDTOEssentials {

    private long id;
    private String pinCode;

    public UnregisteredUserDTOEssentials(UnregisteredUser user) {
        this.id = user.getId();
        this.pinCode = user.getPinCode();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
