package akatsuki.restaurantsysteminformation.room;
import akatsuki.restaurantsysteminformation.room.dto.CreateRoomDTO;
import akatsuki.restaurantsysteminformation.room.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Room> getAll() {
        return roomService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Room getOne(@PathVariable long id) {
        return roomService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateRoomDTO createRoomDTO) {
        Room room = Mapper.convertCreateRoomDTOToRoom(createRoomDTO);
        roomService.create(room);
    }
}
