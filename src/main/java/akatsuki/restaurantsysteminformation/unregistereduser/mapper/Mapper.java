package akatsuki.restaurantsysteminformation.unregistereduser.mapper;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;

public class Mapper {

    public static UnregisteredUser convertUnregisteredUserDTOToUnregisteredUser(UnregisteredUserDTO unregisteredUserDTO) {
        try {
            UserType.valueOf(unregisteredUserDTO.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserTypeNotValidException("User type for unregistered user is not valid.");
        }
        return new UnregisteredUser(
                unregisteredUserDTO.getFirstName(),
                unregisteredUserDTO.getLastName(),
                unregisteredUserDTO.getEmailAddress(),
                unregisteredUserDTO.getPhoneNumber(),
                unregisteredUserDTO.getSalary(),
                UserType.valueOf(unregisteredUserDTO.getType().toUpperCase()),
                false,
                unregisteredUserDTO.getPinCode()
        );
    }

}
