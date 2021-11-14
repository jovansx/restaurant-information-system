package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.dto.CreateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.UpdateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomWithTablesDTO;
import akatsuki.restaurantsysteminformation.room.dto.UpdateRoomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<RoomWithTablesDTO> getAll() {
        return roomService.getAll().stream().map(RoomWithTablesDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomWithTablesDTO getOne(@PathVariable long id) {
        return new RoomWithTablesDTO(roomService.getOne(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody RoomDTO roomDTO) {
        roomService.create(new Room(roomDTO));
    }

    //TODO vidi s simicem sta cemo s ovim
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody UpdateRoomDTO updateRoomDTO, @PathVariable long id) {
        List<CreateRestaurantTableDTO> newTablesDTO = updateRoomDTO.getNewTables();
        List<RestaurantTable> allTables = new ArrayList<>();
        newTablesDTO.forEach(tableDTO -> allTables.add(restaurantTableService.create(new RestaurantTable(tableDTO))));

        List<UpdateRestaurantTableDTO> updateTablesDTO = updateRoomDTO.getUpdateTables();
        updateTablesDTO.forEach(tableDTO -> {
            RestaurantTable table = new RestaurantTable(tableDTO);
            roomService.checkTableInRoom(tableDTO.getId(), id);
            allTables.add(restaurantTableService.update(table, tableDTO.getId()));
        });

        List<Long> deleteTableIds = updateRoomDTO.getDeleteTables();
        deleteTableIds.forEach(tableId -> {
            roomService.checkTableInRoom(tableId, id);
            restaurantTableService.delete(tableId);
        });
        List<RestaurantTable> roomTables = roomService.getRoomTables(id);
        roomTables.forEach(table -> {
            if (!allTables.contains(table)) {
                allTables.add(table);
            }
        });
        Room room = new Room(updateRoomDTO.getName(), false, allTables);
        roomService.update(room, id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        roomService.delete(id);
    }
}
