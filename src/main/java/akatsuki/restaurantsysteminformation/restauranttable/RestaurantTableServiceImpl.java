package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableExistsException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotFoundException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableStateNotValidException;
import akatsuki.restaurantsysteminformation.room.Room;
import akatsuki.restaurantsysteminformation.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements RestaurantTableService {
    private final RestaurantTableRepository restaurantTableRepository;
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
    public RestaurantTable getOneByNameWithOrder(String name) {
        return restaurantTableRepository.findByNameAndFetchOrder(name).orElseThrow(
                () -> new RestaurantTableNotFoundException("Restaurant table the name " + name + " is not found in the database.")
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

        return restaurantTableRepository.save(table);
    }

    @Override
    public void changeStateOfTableWithOrder(Order order) {
        RestaurantTable table = restaurantTableRepository.findByActiveOrder(order).orElseThrow(
                () -> new RestaurantTableNotFoundException("Restaurant table with order id " + order.getId() + " is not found in the database."));
        table.setState(TableState.CHANGED);
        restaurantTableRepository.save(table);
    }

    @Override
    public RestaurantTable delete(long id) {
        RestaurantTable table = getOne(id);
        table.setDeleted(true);
        return restaurantTableRepository.save(table);
    }

    @Override
    public Long getActiveOrderIdByTableId(long id) {
        RestaurantTable table = getOneWithOrder(id);
        if (table.getState().equals(TableState.FREE) || table.getActiveOrder() == null)
            throw new RestaurantTableStateNotValidException("Restaurant table the id " + id + " is not taken.");
        return table.getActiveOrder().getId();
    }

    @Override
    public Long getOrderByTableName(String name) {
        RestaurantTable table = getOneByNameWithOrder(name);
        return table.getActiveOrder() == null ? null : table.getActiveOrder().getId();
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

    private void checkNameExistence(String name, long id, long roomId) {
        RestaurantTable table = getTableByNameIfHeIsContainedInRoom(name, roomId);
        if (id == -1 && table != null)
            throw new RestaurantTableExistsException("Restaurant table with the name " + name + " already exists in the database.");

        if (table != null && table.getId() != id)
            throw new RestaurantTableExistsException("Restaurant table with the name " + name + " already exists in the database.");
    }
}
