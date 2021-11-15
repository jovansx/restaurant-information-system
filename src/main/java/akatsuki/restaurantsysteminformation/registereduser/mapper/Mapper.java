package akatsuki.restaurantsysteminformation.registereduser.mapper;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.RegisteredUser;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;

public class Mapper {

    public static RegisteredUser convertRegisteredUserDTOToRegisteredUser(RegisteredUserDTO registeredUserDTO) {
        try {
            UserType.valueOf(registeredUserDTO.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserTypeNotValidException("User type for unregistered user is not valid.");
        }
        return new RegisteredUser(
                registeredUserDTO.getFirstName(),
                registeredUserDTO.getLastName(),
                registeredUserDTO.getEmailAddress(),
                registeredUserDTO.getPhoneNumber(),
                registeredUserDTO.getSalary(),
                UserType.valueOf(registeredUserDTO.getType().toUpperCase()),
                false,
                registeredUserDTO.getUsername(),
                registeredUserDTO.getPassword()
        );
    }
}
