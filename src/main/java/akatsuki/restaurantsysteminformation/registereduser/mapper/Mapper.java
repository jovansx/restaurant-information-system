package akatsuki.restaurantsysteminformation.registereduser.mapper;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.RegisteredUser;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;

public class Mapper {

    public static RegisteredUser convertRegisteredUserDTOToRegisteredUser(RegisteredUserDTO registeredCreateUserDTO) {
        try {
            UserType.valueOf(registeredCreateUserDTO.getType());
        } catch (IllegalArgumentException e) {
            throw new UserTypeNotValidException("User type for unregistered user is not valid.");
        }
        return new RegisteredUser(
                registeredCreateUserDTO.getFirstName(),
                registeredCreateUserDTO.getLastName(),
                registeredCreateUserDTO.getEmailAddress(),
                registeredCreateUserDTO.getPhoneNumber(),
                registeredCreateUserDTO.getSalary(),
                UserType.valueOf(registeredCreateUserDTO.getType()),
                false,
                registeredCreateUserDTO.getUsername(),
                registeredCreateUserDTO.getPassword()
        );
    }
}
