package akatsuki.restaurantsysteminformation.unregistereduser;

import java.util.List;

public interface UnregisteredUserService {

    UnregisteredUser getOne(long id);
    void create(UnregisteredUser unregisteredUser);
    void update(UnregisteredUser unregisteredUser, long id);
    void delete(long id);
    List<UnregisteredUser> getAll();
}
