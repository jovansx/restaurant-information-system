package akatsuki.restaurantsysteminformation.room;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.room.dto.CreateRoomDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomDTO;
import akatsuki.restaurantsysteminformation.room.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;
    private final RestaurantTableService restaurantTableService;

    @Autowired
    public RoomController(RoomService roomService, RestaurantTableService restaurantTableService) {
        this.roomService = roomService;
        this.restaurantTableService = restaurantTableService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RoomDTO> getAll() {
        List<RoomDTO> roomsDTO = new ArrayList<>();
        List<Room> rooms = roomService.getAll();
        rooms.forEach(room -> {
            List<Long> roomTables = roomService.getRoomTableIds(room.getId());
            roomsDTO.add(new RoomDTO(room, roomTables));
        });
        return roomsDTO;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomDTO getOne(@PathVariable long id) {
        Room room = roomService.getOne(id);
        List<Long> roomTables = roomService.getRoomTableIds(id);
        return new RoomDTO(room, roomTables);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateRoomDTO createRoomDTO) {
        Room room = Mapper.convertCreateRoomDTOToRoom(createRoomDTO);
        roomService.create(room);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody RoomDTO updateRoomDTO, @PathVariable long id) {
        List<RestaurantTable> tables = restaurantTableService.getTablesFromIds(updateRoomDTO.getRestaurantTableIds());
        Room room = Mapper.convertUpdateRoomDTOToRoom(updateRoomDTO, tables);
        roomService.update(room, tables, id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        roomService.delete(id);
    }
}
