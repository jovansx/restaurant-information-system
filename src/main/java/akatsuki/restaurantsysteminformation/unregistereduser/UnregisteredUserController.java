package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserEssentialsDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserRepresentationDTO;
import akatsuki.restaurantsysteminformation.user.dto.UserTableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyAuthority('MANAGER', 'SYSTEM_ADMIN')")
    @GetMapping("/{id}")
    public UnregisteredUserRepresentationDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new UnregisteredUserRepresentationDTO(unregisteredUserService.getOne(id));
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'SYSTEM_ADMIN')")
    @GetMapping("/table")
    public List<UserTableDTO> getAllForRowInTable() {
        return unregisteredUserService.getAll().stream().map(UserTableDTO::new).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'SYSTEM_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody @Valid UnregisteredUserDTO unregisteredUserDTO) {
        return unregisteredUserService.create(unregisteredUserDTO).getId().toString();
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'SYSTEM_ADMIN')")
    @PutMapping("/{id}")
    public void update(@RequestBody @Valid UnregisteredUserDTO unregisteredUserDTO,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        unregisteredUserService.update(unregisteredUserDTO, id);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'SYSTEM_ADMIN')")
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