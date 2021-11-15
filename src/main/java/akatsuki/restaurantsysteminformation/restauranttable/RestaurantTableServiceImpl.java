package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.order.Order;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableExistsException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotFoundException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableStateNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements RestaurantTableService {
    private final RestaurantTableRepository restaurantTableRepository;

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
    public Order getActiveOrderByTableId(long id) {
        RestaurantTable table = getOneWithOrder(id);
        if (table.getState().equals(TableState.FREE) || table.getActiveOrder() == null)
            throw new RestaurantTableStateNotValidException("Restaurant table the id " + id + " is not taken.");
        return table.getActiveOrder();
    }

    @Override
    public RestaurantTable create(RestaurantTable restaurantTable) {
        checkNameExistence(restaurantTable.getName(), -1);
        return restaurantTableRepository.save(restaurantTable);
    }

    @Override
    public List<RestaurantTable> getAll() {
        return restaurantTableRepository.findAll();
    }

    @Override
    public RestaurantTable update(RestaurantTable restaurantTable, long id) {
        RestaurantTable table = getOne(id);

        checkNameExistence(restaurantTable.getName(), id);

        table.setName(restaurantTable.getName());
        table.setShape(restaurantTable.getShape());

        return restaurantTableRepository.save(table);
    }

    @Override
    public void delete(long id) {
        RestaurantTable table = getOne(id);
        table.setDeleted(true);
        restaurantTableRepository.save(table);
    }

    private void checkNameExistence(String name, long id) {
        Optional<RestaurantTable> table = restaurantTableRepository.findByName(name);
        if (id == -1 && table.isPresent()) {
            throw new RestaurantTableExistsException("Restaurant table with the name " + name + " already exists in the database.");
        }

        if (table.isPresent() && table.get().getId() != id) {
            throw new RestaurantTableExistsException("Restaurant table with the name " + name + " already exists in the database.");
        }
    }
}
