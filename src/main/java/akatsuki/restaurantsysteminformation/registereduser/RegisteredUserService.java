package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;

import java.util.List;

public interface RegisteredUserService {
    RegisteredUser getOne(long id);

    List<RegisteredUser> getAll();

    RegisteredUser create(RegisteredUserDTO registeredUserDTO);

    RegisteredUser update(RegisteredUserDTO registeredUserDTO, long id);

    RegisteredUser delete(long id);

    void save(RegisteredUser foundUser);
}
