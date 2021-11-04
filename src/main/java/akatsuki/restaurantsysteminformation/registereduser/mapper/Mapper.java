package akatsuki.restaurantsysteminformation.registereduser.mapper;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.registereduser.RegisteredUser;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;

public class Mapper {

    public static RegisteredUser convertRegisteredUserDTOToRegisteredUser(RegisteredUserDTO registeredCreateUserDTO) {
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
