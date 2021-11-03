package akatsuki.restaurantsysteminformation.unregistereduser;

public interface UnregisteredUserService {

    UnregisteredUser getOne(long id);

    UnregisteredUser create(UnregisteredUser unregisteredUser);
}
