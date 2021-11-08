package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserDetailsDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredUserTableDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.mapper.Mapper;
import akatsuki.restaurantsysteminformation.user.User;
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
        modelMapper.typeMap(UnregisteredUser.class, UnregisteredUserTableDTO.class).addMappings(mapper -> {
            mapper.map(User::getName,
                    UnregisteredUserTableDTO::setName);
        });
        return unregisteredUserService.getAll().stream().map(u -> modelMapper.map(u, UnregisteredUserTableDTO.class)).collect(Collectors.toList());
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
