package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredCreateUserDTO;
import akatsuki.restaurantsysteminformation.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("unregistered-user")
public class UnregisteredUserController {
    private UnregisteredUserServiceImpl unregisteredUserService;

    @Autowired
    public UnregisteredUserController(UnregisteredUserServiceImpl unregisteredUserService) {
        this.unregisteredUserService = unregisteredUserService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UnregisteredUser> getAll() {
        return unregisteredUserService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UnregisteredUser getOne(@PathVariable long id) {
        return unregisteredUserService.getOne(id);
    }

    @ResponseBody
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody UnregisteredCreateUserDTO unregisteredCreateUserDTO) {
        UnregisteredUser user = Mapper.convertUnregisteredCreateUserDTOToUnregisteredUser(unregisteredCreateUserDTO);
        unregisteredUserService.create(user);
    }
}
