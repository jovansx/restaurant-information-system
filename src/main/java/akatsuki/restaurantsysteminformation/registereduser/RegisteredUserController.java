package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/registered-user")
@RequiredArgsConstructor
@Validated
public class RegisteredUserController {
    private final RegisteredUserService registeredUserService;

    @GetMapping
    public List<RegisteredUser> getAll() {
        return registeredUserService.getAll();
    }

    @GetMapping("/{id}")
    public RegisteredUser getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return registeredUserService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid RegisteredUserDTO registeredUserDTO) {
        registeredUserService.create(new RegisteredUser(registeredUserDTO));
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid RegisteredUserDTO registeredUserDTO,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        registeredUserService.update(new RegisteredUser(registeredUserDTO), id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        registeredUserService.delete(id);
    }
}
