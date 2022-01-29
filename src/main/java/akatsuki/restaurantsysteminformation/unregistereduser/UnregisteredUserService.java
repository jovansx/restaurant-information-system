package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;

import java.util.List;

public interface UnregisteredUserService {

    UnregisteredUser getOne(long id);

    List<UnregisteredUser> getAll();

    UnregisteredUser create(UnregisteredUserDTO unregisteredUser);

    UnregisteredUser update(UnregisteredUserDTO unregisteredUserDTO, long id);

    UnregisteredUser delete(long id);

    void deleteById(long id);

    void save(UnregisteredUser unregisteredUser);

    UnregisteredUser checkPinCode(String pinCode, UserType type);

}
