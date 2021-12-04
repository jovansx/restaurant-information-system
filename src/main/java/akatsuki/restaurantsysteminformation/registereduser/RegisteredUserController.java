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

    //TODO: verovatno pada test jer sam promenio tip povratne vrednosti
    @GetMapping("/{id}")
    public RegisteredUserDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new RegisteredUserDTO(registeredUserService.getOne(id));
    }

    @GetMapping
    public List<RegisteredUser> getAll() {
        return registeredUserService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody @Valid RegisteredUserDTO registeredUserDTO) {
        return registeredUserService.create(new RegisteredUser(registeredUserDTO)).getId().toString();
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
