package akatsuki.restaurantsysteminformation.unregistereduser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnregisteredUserService {
    private UnregisteredUserRepository unregisteredUserRepository;

    @Autowired
    public void setUnregisteredUserRepository(UnregisteredUserRepository unregisteredUserRepository) {
        this.unregisteredUserRepository = unregisteredUserRepository;
    }
}
