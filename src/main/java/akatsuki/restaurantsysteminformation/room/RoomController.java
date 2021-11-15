package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomCreateDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomUpdateDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomWithTablesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
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
        List<RoomWithTablesDTO> roomsDTO = new ArrayList<>();
        List<Room> rooms = roomService.getAll();
        rooms.forEach(room -> {
            List<RestaurantTable> roomTables = roomService.getRoomTables(room.getId());
            List<RestaurantTableDTO> tablesDTO = roomTables.stream().map(RestaurantTableDTO::new).collect(Collectors.toList());
            roomsDTO.add(new RoomWithTablesDTO(room.getName(), tablesDTO));
        });
        return roomsDTO;
    }

    @GetMapping("/{id}")
    public RoomWithTablesDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        return new RoomWithTablesDTO(roomService.getOne(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid RoomCreateDTO roomDTO) {
        roomService.create(new Room(roomDTO));
    }

//    @PutMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public void update(@RequestBody UpdateRoomDTO updateRoomDTO, @PathVariable long id) {
//        List<CreateRestaurantTableDTO> newTablesDTO = updateRoomDTO.getNewTables();
//        List<RestaurantTable> allTables = new ArrayList<>();
//        newTablesDTO.forEach(tableDTO -> allTables.add(restaurantTableService.create(new RestaurantTable(tableDTO))));
//
//        List<UpdateRestaurantTableDTO> updateTablesDTO = updateRoomDTO.getUpdateTables();
//        updateTablesDTO.forEach(tableDTO -> {
//            RestaurantTable table = new RestaurantTable(tableDTO);
//            roomService.checkTableInRoom(tableDTO.getId(), id);
//            allTables.add(restaurantTableService.update(table, tableDTO.getId()));
//        });
//
//        List<Long> deleteTableIds = updateRoomDTO.getDeleteTables();
//        deleteTableIds.forEach(tableId -> {
//            roomService.checkTableInRoom(tableId, id);
//            restaurantTableService.delete(tableId);
//        });
//        List<RestaurantTable> roomTables = roomService.getRoomTables(id);
//        roomTables.forEach(table -> {
//            if (!allTables.contains(table)) {
//                allTables.add(table);
//            }
//        });
//        Room room = new Room(updateRoomDTO.getName(), false, allTables);
//        roomService.update(room, id);
//    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid RoomUpdateDTO roomUpdateDTO,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        roomService.update(roomUpdateDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        roomService.delete(id);
    }
}
