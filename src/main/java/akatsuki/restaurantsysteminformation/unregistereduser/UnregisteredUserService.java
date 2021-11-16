package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;

import java.util.List;

public interface UnregisteredUserService {

    UnregisteredUser getOne(long id);

    List<UnregisteredUser> getAll();

    void create(UnregisteredUser unregisteredUser);

    void update(UnregisteredUser unregisteredUser, long id);

    void delete(long id);

    UnregisteredUser checkPinCode(String pinCode, UserType type);
}
