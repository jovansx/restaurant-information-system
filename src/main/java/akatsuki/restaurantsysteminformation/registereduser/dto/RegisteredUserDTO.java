package akatsuki.restaurantsysteminformation.registereduser.dto;

import akatsuki.restaurantsysteminformation.user.dto.UserDTO;

public class RegisteredUserDTO extends UserDTO {
    private String username;
    private String password;
    public RegisteredUserDTO(String firstName, String lastName, String emailAddress, String phoneNumber, double salary, String type, String username, String password) {
        super(firstName, lastName, emailAddress, phoneNumber, salary, type);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
