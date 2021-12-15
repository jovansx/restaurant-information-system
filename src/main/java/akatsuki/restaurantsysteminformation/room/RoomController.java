package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.room.dto.RoomCreateDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomLayoutDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomTablesUpdateDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomWithTablesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@Validated
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public List<RoomWithTablesDTO> getAll() {
        return roomService.getAll().stream().map(RoomWithTablesDTO::new).collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody @Valid RoomCreateDTO roomDTO) {
        return roomService.create(new Room(roomDTO)).getId().toString();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/name")
    public void updateRoomName(@RequestBody @NotBlank String newName,
                               @Positive(message = "Id has to be a positive value.") @PathVariable long id) {
        roomService.updateName(newName, id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/layout")
    public void updateRoomLayout(@RequestBody @Valid RoomLayoutDTO layoutDTO,
                                 @Positive(message = "Id has to be a positive value.") @PathVariable long id) {
        roomService.updateLayout(layoutDTO, id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/tables")
    public void updateRoomTables(@RequestBody @Valid RoomTablesUpdateDTO updateRoomDTO,
                                 @Positive(message = "Id has to be a positive value.") @PathVariable long id) {
        roomService.updateTables(updateRoomDTO, id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        roomService.delete(id);
    }
}
