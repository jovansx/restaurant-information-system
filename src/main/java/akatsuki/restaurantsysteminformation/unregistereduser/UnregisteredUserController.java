package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserEssentialsDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserTableDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping
    public List<UnregisteredUser> getAll() {
        return unregisteredUserService.getAll();
    }

    @GetMapping("/table")
    public List<UnregisteredUserTableDTO> getAllForTable() {
        return unregisteredUserService.getAll().stream().map(UnregisteredUserTableDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UnregisteredUserDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new UnregisteredUserDTO(unregisteredUserService.getOne(id));
    }

    @GetMapping("/pin-code/{pinCode}")
    public UnregisteredUserEssentialsDTO checkPinCode(@PathVariable @Pattern(regexp = "[0-9]{4}", message = "It has to be 4 digits number.") String pinCode,
                                                      @RequestParam("usertype") String userType) {
        return new UnregisteredUserEssentialsDTO(unregisteredUserService.checkPinCode(pinCode, UserType.valueOf(userType.toUpperCase())));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid UnregisteredUserDTO unregisteredUserDTO) {
        UnregisteredUser user = Mapper.convertUnregisteredUserDTOToUnregisteredUser(unregisteredUserDTO);
        unregisteredUserService.create(user);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid UnregisteredUserDTO unregisteredUserDTO,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        UnregisteredUser user = Mapper.convertUnregisteredUserDTOToUnregisteredUser(unregisteredUserDTO);
        unregisteredUserService.update(user, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        unregisteredUserService.delete(id);
    }
}