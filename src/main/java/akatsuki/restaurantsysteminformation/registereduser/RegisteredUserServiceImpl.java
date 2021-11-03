package akatsuki.restaurantsysteminformation.registereduser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisteredUserServiceImpl implements RegisteredUserService {
    private RegisteredUserRepository registeredUserRepository;

    @Autowired
    public void setRegisteredUserRepository(RegisteredUserRepository registeredUserRepository) {
        this.registeredUserRepository = registeredUserRepository;
    }
}
