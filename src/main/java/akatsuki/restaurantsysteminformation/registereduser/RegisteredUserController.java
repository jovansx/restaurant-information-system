package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserChangePasswordDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDetailsDTO;
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
    public RegisteredUserDetailsDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new RegisteredUserDetailsDTO(registeredUserService.getOne(id));
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
    public void update(@RequestBody @Valid RegisteredUserDetailsDTO registeredUserDTO,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        registeredUserService.update(new RegisteredUser(registeredUserDTO), id);
    }

    //TODO: naknadno dodato
    @PutMapping("/change-password/{id}")
    public void changePassword(@RequestBody @Valid RegisteredUserChangePasswordDTO registeredUserDTO,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        registeredUserService.changePassword(registeredUserDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        registeredUserService.delete(id);
    }
}
