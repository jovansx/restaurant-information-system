package akatsuki.restaurantsysteminformation.user;

import akatsuki.restaurantsysteminformation.user.dto.UserTableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('MANAGER', 'SYSTEM_ADMIN')")
    @GetMapping("/table")
    public List<UserTableDTO> getAllForRowInTable() {
        return userService.getAllManagersAndUnregistered().stream().map(UserTableDTO::new).collect(Collectors.toList());
    }
}
