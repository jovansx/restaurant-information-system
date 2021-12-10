package akatsuki.restaurantsysteminformation.registereduser;

import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserChangePasswordDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDTO;
import akatsuki.restaurantsysteminformation.registereduser.dto.RegisteredUserDetailsDTO;
import akatsuki.restaurantsysteminformation.user.dto.UserTableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registered-user")
@RequiredArgsConstructor
@Validated
public class RegisteredUserController {
    private final RegisteredUserService registeredUserService;

    //TODO: verovatno pada test jer sam promenio tip povratne vrednosti
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'ADMIN')")
    @GetMapping("/{id}")
    public RegisteredUserDetailsDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new RegisteredUserDetailsDTO(registeredUserService.getOne(id));
    }

    //    TODO Vidi da li se koristi
    @GetMapping
    public List<RegisteredUser> getAll() {
        return registeredUserService.getAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/system-admin/table")
    public List<UserTableDTO> getAllSystemAdminsForRowInTable() {
        return registeredUserService.getAllSystemAdmins().stream().map(UserTableDTO::new).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody @Valid RegisteredUserDTO registeredUserDTO) {
        return registeredUserService.create(registeredUserDTO).getId().toString();
    }

    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'ADMIN')")
    @PutMapping("/{id}")
    public void update(@RequestBody @Valid RegisteredUserDetailsDTO registeredUserDTO,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        registeredUserService.update(registeredUserDTO, id);
    }

    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'ADMIN')")
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
