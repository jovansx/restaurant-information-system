package akatsuki.restaurantsysteminformation.registereduser;

import java.util.List;

public interface RegisteredUserService {
    RegisteredUser getOne(long id);

    List<RegisteredUser> getAll();

    RegisteredUser create(RegisteredUser registeredUser);

    RegisteredUser update(RegisteredUser registeredUser, long id);

    RegisteredUser delete(long id);

    void save(RegisteredUser foundUser);
}
