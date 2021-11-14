package akatsuki.restaurantsysteminformation.unregistereduser.mapper;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.user.exception.UserTypeNotValidException;

public class Mapper {

    public static UnregisteredUser convertUnregisteredUserDTOToUnregisteredUser(UnregisteredUserDTO unregisteredUserDTO) {
        return new UnregisteredUser(
                unregisteredUserDTO.getFirstName(),
                unregisteredUserDTO.getLastName(),
                unregisteredUserDTO.getEmailAddress(),
                unregisteredUserDTO.getPhoneNumber(),
                unregisteredUserDTO.getSalary(),
                unregisteredUserDTO.getType()),
                false,
                unregisteredUserDTO.getPinCode()
        );
    }

}
