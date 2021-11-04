package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredCreateUserDTO;
import akatsuki.restaurantsysteminformation.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/unregistered-user")
public class UnregisteredUserController {
    private final UnregisteredUserServiceImpl unregisteredUserService;

    @Autowired
    public UnregisteredUserController(UnregisteredUserServiceImpl unregisteredUserService) {
        this.unregisteredUserService = unregisteredUserService;
    }

    @GetMapping("/{id}")
    public UnregisteredUser getOne(@PathVariable long id) {
        return unregisteredUserService.getOne(id);
    }

    @PostMapping
    public UnregisteredUser create(@RequestBody UnregisteredCreateUserDTO unregisteredCreateUserDTO) {
        UnregisteredUser user = Mapper.convertUnregisteredCreateUserDTOToUnregisteredUser(unregisteredCreateUserDTO);
        return unregisteredUserService.create(user);
    }
}
