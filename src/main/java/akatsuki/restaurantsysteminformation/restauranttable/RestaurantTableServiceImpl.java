package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableExistsException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotFoundException;
import akatsuki.restaurantsysteminformation.room.Room;
import akatsuki.restaurantsysteminformation.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {
    @Autowired
    private RestaurantTableRepository restaurantTableRepository;
    private RoomService roomService;

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public RestaurantTable getOne(long id) {
        return restaurantTableRepository.findById(id).orElseThrow(
                () -> new RestaurantTableNotFoundException("Restaurant table the id " + id + " is not found in the database.")
        );
    }

    @Override
    public RestaurantTable getOneWithOrder(long id) {
        return restaurantTableRepository.findByIdAndFetchOrder(id).orElseThrow(
                () -> new RestaurantTableNotFoundException("Restaurant table the id " + id + " is not found in the database.")
        );
    }

    @Override
    public List<RestaurantTable> getAll() {
        return restaurantTableRepository.findAll();
    }

    @Override
    public RestaurantTable create(RestaurantTable restaurantTable, long roomId) {
        checkNameExistence(restaurantTable.getName(), -1, roomId);
        return restaurantTableRepository.save(restaurantTable);
    }

    @Override
    public RestaurantTable update(RestaurantTable restaurantTable, long id, long roomId) {
        RestaurantTable table = getOne(id);
        checkNameExistence(restaurantTable.getName(), id, roomId);

        table.setName(restaurantTable.getName());
        table.setShape(restaurantTable.getShape());
        table.setColumn(restaurantTable.getColumn());
        table.setRow(restaurantTable.getRow());

        return restaurantTableRepository.save(table);
    }

    @Override
    public RestaurantTable changeStateOfTableWithOrder(Order order, TableState state) {
        RestaurantTable table = restaurantTableRepository.findByActiveOrder(order).orElseThrow(
                () -> new RestaurantTableNotFoundException("Restaurant table with order id " + order.getId() + " is not found in the database."));
        table.setState(state);

        if (state.equals(TableState.FREE))
            table.setActiveOrder(null);

        return restaurantTableRepository.save(table);
    }

    @Override
    public RestaurantTable setOrderToTable(Long tableId, Order order) {
        RestaurantTable table = getOne(tableId);
        table.setActiveOrder(order);
        table.setState(TableState.TAKEN);
        return restaurantTableRepository.save(table);
    }

    @Override
    public RestaurantTable delete(long id) {
        RestaurantTable table = getOne(id);
        table.setDeleted(true);
        return restaurantTableRepository.save(table);
    }

    private void checkNameExistence(String name, long id, long roomId) {
        RestaurantTable table = getTableByNameIfHeIsContainedInRoom(name, roomId);
        if (id == -1 && table != null)
            throw new RestaurantTableExistsException("Restaurant table with the name " + name + " already exists in the database.");

        if (table != null && table.getId() != id)
            throw new RestaurantTableExistsException("Restaurant table with the name " + name + " already exists in the database.");
    }

    private RestaurantTable getTableByNameIfHeIsContainedInRoom(String name, long roomId) {
        Room foundRoom = roomService.getOne(roomId);
        RestaurantTable table = null;
        for (RestaurantTable restaurantTable : foundRoom.getRestaurantTables()) {
            if (restaurantTable.getName().equals(name)) {
                table = restaurantTable;
            }
        }
        return table;
    }
}
