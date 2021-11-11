package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTOEssentials;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDetailsDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserTableDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/unregistered-user")
public class UnregisteredUserController {
    private final UnregisteredUserService unregisteredUserService;
    private final ModelMapper modelMapper;

    @Autowired
    public UnregisteredUserController(UnregisteredUserService unregisteredUserService, ModelMapper modelMapper) {
        this.unregisteredUserService = unregisteredUserService;
        this.modelMapper = modelMapper;
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
    public UnregisteredUserDetailsDTO getDetailsById(@PathVariable long id) {
        UnregisteredUser unregisteredUser = unregisteredUserService.getOne(id);
        return modelMapper.map(unregisteredUser, UnregisteredUserDetailsDTO.class);
    }

    @GetMapping("/pin-code/{pinCode}")
    @ResponseStatus(HttpStatus.OK)
    public UnregisteredUserDTOEssentials checkPinCode(@PathVariable int pinCode, @RequestParam("usertype") String userType) {
        return new UnregisteredUserDTOEssentials(unregisteredUserService.checkPinCode(pinCode, UserType.valueOf(userType.toUpperCase())));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody UnregisteredUserDTO unregisteredUserDTO) {
        UnregisteredUser user = Mapper.convertUnregisteredUserDTOToUnregisteredUser(unregisteredUserDTO);
        unregisteredUserService.create(user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody UnregisteredUserDTO unregisteredUserDTO, @PathVariable long id) {
        UnregisteredUser user = Mapper.convertUnregisteredUserDTOToUnregisteredUser(unregisteredUserDTO);
        unregisteredUserService.update(user, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        unregisteredUserService.delete(id);
    }
}
