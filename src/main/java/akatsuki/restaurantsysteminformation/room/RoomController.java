package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableCreateDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomCreateDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomWithTablesDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomUpdateDTO;
import akatsuki.restaurantsysteminformation.room.helper.Helper;
import akatsuki.restaurantsysteminformation.room.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@Validated
public class RoomController {
    private final RoomService roomService;
    private final RestaurantTableService restaurantTableService;

    @GetMapping
    public List<RoomWithTablesDTO> getAll() {
        List<RoomWithTablesDTO> roomsDTO = new ArrayList<>();
        List<Room> rooms = roomService.getAll();
        rooms.forEach(room -> {
            List<RestaurantTable> roomTables = roomService.getRoomTables(room.getId());
            List<RestaurantTableDTO> tablesDTO = Helper.getTablesDTO(roomTables);
            roomsDTO.add(new RoomWithTablesDTO(room.getName(), tablesDTO));
        });
        return roomsDTO;
    }

    @GetMapping("/{id}")
    public RoomWithTablesDTO getOne(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        Room room = roomService.getOne(id);
        List<RestaurantTable> roomTables = roomService.getRoomTables(id);
        List<RestaurantTableDTO> tablesDTO = Helper.getTablesDTO(roomTables);
        return new RoomWithTablesDTO(room.getName(), tablesDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid RoomCreateDTO roomDTO) {
        Room room = Mapper.convertRoomDTOToRoom(roomDTO);
        roomService.create(room);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid RoomUpdateDTO updateRoomDTO,
                       @PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        // idi kroz nove stolove i dodaj ih u table repo
        List<RestaurantTableCreateDTO> newTablesDTO = updateRoomDTO.getNewTables();
        List<RestaurantTable> allTables = new ArrayList<>();
        newTablesDTO.forEach(tableDTO -> {
            RestaurantTable table = Mapper.convertCreateRestaurantTableDTOToRestaurantTable(tableDTO);
            RestaurantTable savedTable = restaurantTableService.create(table);
            allTables.add(savedTable);
        });

        List<RestaurantTableDTO> updateTablesDTO = updateRoomDTO.getUpdateTables();
        updateTablesDTO.forEach(tableDTO -> {
            RestaurantTable table = Mapper.convertCreateRestaurantTableDTOToRestaurantTable(tableDTO);
            // vidi da li su dobri id-jevi od stolova za update (moraju vec postojati u room.tables)
            roomService.checkTableInRoom(tableDTO.getId(), id);
            // idi kroz update stolove i dodaj ih u table repo
            RestaurantTable updatedTable = restaurantTableService.update(table, tableDTO.getId());
            allTables.add(updatedTable);
        });

        List<Long> deleteTableIds = updateRoomDTO.getDeleteTables();
        deleteTableIds.forEach(tableId -> {
            // vidi da li su dobri id-jevi od stolova za brisanje (moraju vec postojati u room.tables)
            roomService.checkTableInRoom(tableId, id);
            // idi kroz delete stolove i obrisi ih u table repo
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
    public void delete(@PathVariable @Positive(message = "Id has to be a positive value.") long id) {
        roomService.delete(id);
    }
}
