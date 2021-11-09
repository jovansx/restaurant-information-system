package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTOEssentials;
import akatsuki.restaurantsysteminformation.unregistereduser.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unregistered-user")
public class UnregisteredUserController {
    private final UnregisteredUserService unregisteredUserService;

    @Autowired
    public UnregisteredUserController(UnregisteredUserService unregisteredUserService) {
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

    @GetMapping("/pin-code/{pinCode}")
    @ResponseStatus(HttpStatus.OK)
    public UnregisteredUserDTOEssentials checkPinCode(@PathVariable int pinCode, @RequestParam("usertype") String userType) {
        return new UnregisteredUserDTOEssentials(unregisteredUserService.checkPinCode(pinCode, UserType.valueOf(userType.toUpperCase())));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody UnregisteredUserDTO unregisteredUserDTO) {
        UnregisteredUser user = Mapper.convertUnregisteredUserDTOToUnregisteredUser(unregisteredUserDTO);
        unregisteredUserService.create(user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody UnregisteredUserDTO unregisteredUserDTO, @PathVariable long id) {
        UnregisteredUser user = Mapper.convertUnregisteredUserDTOToUnregisteredUser(unregisteredUserDTO);
        unregisteredUserService.update(user, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        unregisteredUserService.delete(id);
    }
}
