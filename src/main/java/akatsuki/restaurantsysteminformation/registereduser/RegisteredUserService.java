package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserChangePasswordDTO;

import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDetailsDTO;
import akatsuki.restaurantsysteminformation.user.User;

import java.util.List;

public interface RegisteredUserService {
    RegisteredUser getOne(long id);

    List<RegisteredUser> getAll();

    List<User> getAllSystemAdmins();

    RegisteredUser create(RegisteredUserDTO registeredUserDTO);

    RegisteredUser update(RegisteredUserDetailsDTO registeredUserDTO, long id);

    RegisteredUser delete(long id);

    RegisteredUser changePassword(RegisteredUserChangePasswordDTO dto, long id);

    void deleteById(long id);

    void save(RegisteredUser foundUser);
}
