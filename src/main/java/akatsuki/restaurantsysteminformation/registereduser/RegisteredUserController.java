package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registered-user")
public class RegisteredUserController {
    private final RegisteredUserService registeredUserService;

    @Autowired
    public RegisteredUserController(RegisteredUserService registeredUserService) {
        this.registeredUserService = registeredUserService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RegisteredUser> getAll() {
        return registeredUserService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RegisteredUser getOne(@PathVariable long id) {
        return registeredUserService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody RegisteredUserDTO registeredUserDTO) {
        registeredUserService.create(new RegisteredUser(registeredUserDTO));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody RegisteredUserDTO registeredUserDTO, @PathVariable long id) {
        registeredUserService.update(new RegisteredUser(registeredUserDTO), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        registeredUserService.delete(id);
    }
}
