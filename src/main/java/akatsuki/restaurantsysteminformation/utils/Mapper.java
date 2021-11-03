package akatsuki.restaurantsysteminformation.utils;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredCreateUserDTO;

public class Mapper {

    public static UnregisteredUser convertUnregisteredCreateUserDTOToUnregisteredUser(UnregisteredCreateUserDTO unregisteredCreateUserDTO) {
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
