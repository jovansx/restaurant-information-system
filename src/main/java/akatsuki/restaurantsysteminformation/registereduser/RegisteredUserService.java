package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserChangePasswordDTO;

import java.util.List;

public interface RegisteredUserService {
    RegisteredUser getOne(long id);

    List<RegisteredUser> getAll();

    RegisteredUser create(RegisteredUser registeredUser);

    RegisteredUser update(RegisteredUser registeredUser, long id);

    RegisteredUser delete(long id);

    RegisteredUser changePassword(RegisteredUserChangePasswordDTO dto, long id);

    void save(RegisteredUser foundUser);
}
