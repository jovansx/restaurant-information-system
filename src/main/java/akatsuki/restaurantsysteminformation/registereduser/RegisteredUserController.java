package akatsuki.restaurantsysteminformation.registereduser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registered-user")
public class RegisteredUserController {
    private RegisteredUserServiceImpl registeredUserService;

    @Autowired
    public RegisteredUserController(RegisteredUserServiceImpl registeredUserService) {
        this.registeredUserService = registeredUserService;
    }
}
