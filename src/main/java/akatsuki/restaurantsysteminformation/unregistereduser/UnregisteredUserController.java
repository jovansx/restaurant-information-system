package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTOEssentials;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserTableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/table")
    @ResponseStatus(HttpStatus.OK)
    public List<UnregisteredUserTableDTO> getAllForTable() {
        return unregisteredUserService.getAll().stream().map(UnregisteredUserTableDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UnregisteredUser getOne(@PathVariable long id) {
        return unregisteredUserService.getOne(id);
    }

    @GetMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UnregisteredUserDTO getDetailsById(@PathVariable long id) {
        return new UnregisteredUserDTO(unregisteredUserService.getOne(id));
    }

    @GetMapping("/pin-code/{pinCode}")
    @ResponseStatus(HttpStatus.OK)
    public UnregisteredUserDTOEssentials checkPinCode(@PathVariable int pinCode, @RequestParam("usertype") String userType) {
        return new UnregisteredUserDTOEssentials(unregisteredUserService.checkPinCode(pinCode, UserType.valueOf(userType.toUpperCase())));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody UnregisteredUserDTO unregisteredUserDTO) {
        unregisteredUserService.create(new UnregisteredUser(unregisteredUserDTO));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody UnregisteredUserDTO unregisteredUserDTO, @PathVariable long id) {
        unregisteredUserService.update(new UnregisteredUser(unregisteredUserDTO), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        unregisteredUserService.delete(id);
    }
}
