package akatsuki.restaurantsysteminformation.registereduser;

import java.util.List;

public interface RegisteredUserService {
    List<RegisteredUser> getAll();
    RegisteredUser getOne(long id);
    void create(RegisteredUser registeredUser);
    void update(RegisteredUser registeredUser, long id);
    void delete(long id);
}
