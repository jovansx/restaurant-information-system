package akatsuki.restaurantsysteminformation.unregistereduser;

import java.util.List;

public interface UnregisteredUserService {

    UnregisteredUser getOne(long id);
    void create(UnregisteredUser unregisteredUser);
    List<UnregisteredUser> getAll();
}
