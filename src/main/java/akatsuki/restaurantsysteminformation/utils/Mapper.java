package akatsuki.restaurantsysteminformation.utils;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Mapper {

    public static UnregisteredUser convertUnregisteredUserDTOToUnregisteredUser(UnregisteredUserDTO unregisteredCreateUserDTO) {
        try {
            UserType.valueOf(unregisteredCreateUserDTO.getType());
        } catch (IllegalArgumentException e) {
            throw new UserTypeNotValidException("User type for unregistered user is not valid.");
        }
        return new UnregisteredUser(
                unregisteredCreateUserDTO.getFirstName(),
                unregisteredCreateUserDTO.getLastName(),
                unregisteredCreateUserDTO.getEmailAddress(),
                unregisteredCreateUserDTO.getPhoneNumber(),
                unregisteredCreateUserDTO.getSalary(),
                UserType.valueOf(unregisteredCreateUserDTO.getType()),
                false,
                unregisteredCreateUserDTO.getPinCode()
        );
    }
}
