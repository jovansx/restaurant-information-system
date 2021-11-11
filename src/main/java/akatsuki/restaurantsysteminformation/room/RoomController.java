package akatsuki.restaurantsysteminformation.room;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.dto.CreateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableRepresentationDTO;
import akatsuki.restaurantsysteminformation.restauranttable.dto.UpdateRestaurantTableDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomWithTablesDTO;
import akatsuki.restaurantsysteminformation.room.dto.UpdateRoomDTO;
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
    public List<RoomWithTablesDTO> getAll() {
        List<RoomWithTablesDTO> roomsDTO = new ArrayList<>();
        List<Room> rooms = roomService.getAll();
        rooms.forEach(room -> {
            List<RestaurantTable> roomTables = roomService.getRoomTables(room.getId());
            List<RestaurantTableRepresentationDTO> tablesDTO = new ArrayList<>();
            roomTables.forEach(table -> {
                tablesDTO.add(new RestaurantTableRepresentationDTO(table));
            });
            roomsDTO.add(new RoomWithTablesDTO(room.getName(), tablesDTO));
        });
        return roomsDTO;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomWithTablesDTO getOne(@PathVariable long id) {
        Room room = roomService.getOne(id);
        List<RestaurantTable> roomTables = roomService.getRoomTables(id);
        List<RestaurantTableRepresentationDTO> tablesDTO = new ArrayList<>();
        roomTables.forEach(table -> {
            tablesDTO.add(new RestaurantTableRepresentationDTO(table));
        });
        return new RoomWithTablesDTO(room.getName(), tablesDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody RoomDTO roomDTO) {
        // kreiranje stola - automatski se sacuva sa praznom listom stolova
        Room room = Mapper.convertRoomDTOToRoom(roomDTO);
        roomService.create(room);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody UpdateRoomDTO updateRoomDTO, @PathVariable long id) {
        // idi kroz nove stolove i dodaj ih u table repo
        List<CreateRestaurantTableDTO> newTablesDTO = updateRoomDTO.getNewTables();
        List<RestaurantTable> allTables = new ArrayList<>();
        newTablesDTO.forEach(tableDTO -> {
            RestaurantTable table = Mapper.convertCreateRestaurantTableDTOToRestaurantTable(tableDTO);
            RestaurantTable savedTable = restaurantTableService.create(table);
            allTables.add(savedTable);
        });

        List<UpdateRestaurantTableDTO> updateTablesDTO = updateRoomDTO.getUpdateTables();
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
            if(!allTables.contains(table)) {
                allTables.add(table);
            }
        });
        Room room = new Room(updateRoomDTO.getName(), false, allTables);

        // posalji taj room u roomservice.update
        roomService.update(room, id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        roomService.delete(id);
    }
}
