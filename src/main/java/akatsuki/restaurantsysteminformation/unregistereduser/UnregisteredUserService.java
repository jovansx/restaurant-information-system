package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;

import java.util.List;

public interface UnregisteredUserService {

    UnregisteredUser getOne(long id);

    List<UnregisteredUser> getAll();

    UnregisteredUser create(UnregisteredUser unregisteredUser);

    UnregisteredUser update(UnregisteredUser unregisteredUser, long id);

    UnregisteredUser delete(long id);

    UnregisteredUser checkPinCode(String pinCode, UserType type);
}
