package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTableService;
import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotAvailableException;
import akatsuki.restaurantsysteminformation.room.dto.RoomLayoutDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomTablesUpdateDTO;
import akatsuki.restaurantsysteminformation.room.exception.RoomDeletionFailedException;
import akatsuki.restaurantsysteminformation.room.exception.RoomExistsException;
import akatsuki.restaurantsysteminformation.room.exception.RoomLayoutUpdateException;
import akatsuki.restaurantsysteminformation.room.exception.RoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RestaurantTableService restaurantTableService;

    @Override
    public Room getOne(long id) {
        return roomRepository.findById(id).orElseThrow(
                () -> new RoomNotFoundException("Room the id " + id + " is not found in the database.")
        );
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room create(Room room) {
        checkNameExistence(room.getName(), -1);
        return roomRepository.save(room);
    }

    @Override
    public Room delete(long id) {
        Room room = getOne(id);
        room.setDeleted(true);
        room.getRestaurantTables().forEach(table -> {
            if (table.getActiveOrder() != null)
                throw new RoomDeletionFailedException("Room with the id " + id + " cannot be deleted because it has active order.");
            restaurantTableService.delete(table.getId());
        });
        return roomRepository.save(room);
    }

    @Override
    public Room updateTables(RoomTablesUpdateDTO roomDTO, long id) {
        Room room = getOne(id);

        List<RestaurantTableDTO> adding = new ArrayList<>();
        List<RestaurantTableDTO> editing = new ArrayList<>();
        List<RestaurantTable> deleting = room.getRestaurantTables();

        for (RestaurantTableDTO t : roomDTO.getTables()) {
            if (t.getId() != 0) {
                editing.add(t);
                deleting = deleting.stream().filter(tb -> !tb.getId().equals(t.getId())).collect(Collectors.toList());
                continue;
            }
            List<RestaurantTable> filteredList = room.getRestaurantTables().stream()
                    .filter(tb -> tb.getName().equals(t.getName())).collect(Collectors.toList());
            if (filteredList.isEmpty())
                adding.add(t);
            else {
                t.setId(filteredList.get(0).getId());
                editing.add(t);
            }
            deleting = deleting.stream().filter(tb -> !tb.getName().equals(t.getName())).collect(Collectors.toList());
        }

        List<RestaurantTable> tables = createNewTables(adding, id);
        List<RestaurantTable> updatedTables = updateTables(editing, id);
        tables.addAll(updatedTables);

        for (RestaurantTable r : deleting) {
            restaurantTableService.delete(r.getId());
        }

        room.setRestaurantTables(tables);
        return roomRepository.save(room);
    }

    @Override
    public Room updateName(String newName, long id) {
        checkNameExistence(newName, id);
        Room foundRoom = getOne(id);
        foundRoom.setName(newName);
        return roomRepository.save(foundRoom);
    }

    @Override
    public Room updateLayout(RoomLayoutDTO layoutDTO, long id) {
        Room foundRoom = getOne(id);

        boolean tableNotContainedInNewLayout = false;
        for (RestaurantTable table : foundRoom.getRestaurantTables()) {
            if (table.getRow() + 1 > layoutDTO.getRows()) {
                tableNotContainedInNewLayout = true;
            }
            if (table.getColumn() + 1 > layoutDTO.getColumns()) {
                tableNotContainedInNewLayout = true;
            }
        }
        if (tableNotContainedInNewLayout)
            throw new RoomLayoutUpdateException("Room cannot be updated because existing tables are not contained in a new grid.");

        foundRoom.setRows(layoutDTO.getRows());
        foundRoom.setColumns(layoutDTO.getColumns());
        return roomRepository.save(foundRoom);
    }

    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }

    private List<RestaurantTable> createNewTables(List<RestaurantTableDTO> newTablesDTO, Long roomId) {
        List<RestaurantTable> tables = new ArrayList<>();
        newTablesDTO.forEach(tableDTO -> {
            RestaurantTable table = restaurantTableService.create(new RestaurantTable(tableDTO), roomId);
            tables.add(table);
        });
        return tables;
    }

    private List<RestaurantTable> updateTables(List<RestaurantTableDTO> updateTablesDTO, long id) {
        List<RestaurantTable> tables = new ArrayList<>();
        updateTablesDTO.forEach(tableDTO -> {
            RestaurantTable table = new RestaurantTable(tableDTO);
            checkTableInRoom(tableDTO.getId(), id);
            tables.add(restaurantTableService.update(table, tableDTO.getId(), 1L));
        });
        return tables;
    }

    private void checkNameExistence(String name, long id) {
        Optional<Room> room = roomRepository.findByName(name);
        if (id == -1 && room.isPresent())
            throw new RoomExistsException("Room with the name " + name + " already exists in the database.");

        if (room.isPresent() && room.get().getId() != id)
            throw new RoomExistsException("Room with the name " + name + " already exists in the database.");
    }

    private void checkTableInRoom(long tableId, long id) {
        Room room = getOne(id);
        RestaurantTable table = restaurantTableService.getOne(tableId);

        if (!room.getRestaurantTables().contains(table))
            throw new RestaurantTableNotAvailableException("Restaurant table with the id " + table.getId() + " is not available in the room " + room.getName());
    }

}
