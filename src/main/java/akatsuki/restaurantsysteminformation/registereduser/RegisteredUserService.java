package akatsuki.restaurantsysteminformation.registereduser;

import java.util.List;

public interface RegisteredUserService {
    RegisteredUser getOne(long id);

    List<RegisteredUser> getAll();

    void create(RegisteredUser registeredUser);

    void update(RegisteredUser registeredUser, long id);

    void delete(long id);
}
