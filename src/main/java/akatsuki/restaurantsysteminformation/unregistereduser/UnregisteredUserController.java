package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserEssentialsDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserTableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/unregistered-user")
@RequiredArgsConstructor
@Validated
public class UnregisteredUserController {
    private final UnregisteredUserService unregisteredUserService;

    @GetMapping("/{id}")
    public UnregisteredUserDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new UnregisteredUserDTO(unregisteredUserService.getOne(id));
    }

    //TODO Ovde nema DTO-a
    @GetMapping
    public List<UnregisteredUser> getAll() {
        return unregisteredUserService.getAll();
    }

    @GetMapping("/table")
    public List<UnregisteredUserTableDTO> getAllForRowInTable() {
        return unregisteredUserService.getAll().stream().map(UnregisteredUserTableDTO::new).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid UnregisteredUserDTO unregisteredUserDTO) {
        unregisteredUserService.create(new UnregisteredUser(unregisteredUserDTO));
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid UnregisteredUserDTO unregisteredUserDTO,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        unregisteredUserService.update(new UnregisteredUser(unregisteredUserDTO), id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        unregisteredUserService.delete(id);
    }

    @GetMapping("/pin-code/{pinCode}")
    public UnregisteredUserEssentialsDTO checkPinCode(@PathVariable @Pattern(regexp = "[0-9]{4}", message = "It has to be 4 digits number.") String pinCode,
                                                      @RequestParam("usertype") @NotEmpty(message = "It cannot be empty.") String userType) {
        return new UnregisteredUserEssentialsDTO(unregisteredUserService.checkPinCode(pinCode, UserType.valueOf(userType.toUpperCase())));
    }
}