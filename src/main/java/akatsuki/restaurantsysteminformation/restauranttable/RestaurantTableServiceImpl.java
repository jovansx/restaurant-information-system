package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableExistsException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotFoundException;
import akatsuki.restaurantsysteminformation.room.Room;
import akatsuki.restaurantsysteminformation.room.exception.RoomExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    public void setRestaurantTableRepository(RestaurantTableRepository restaurantTableRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
    }

    @Override
    public List<RestaurantTable> getTablesFromIds(List<Long> tableIds) {
        List<RestaurantTable> tables = new ArrayList<>();
        tableIds.forEach(tableId -> {
            Optional<RestaurantTable> tableMaybe = restaurantTableRepository.findById(tableId);
            if(tableMaybe.isEmpty()) {
                throw new RestaurantTableNotFoundException("Table with the id " + tableId + " is not found in the database.");
            }
            tables.add(tableMaybe.get());
        });
        return tables;
    }

    @Override
    public void create(RestaurantTable restaurantTable) {
        checkNameExistence(restaurantTable.getName());
        restaurantTableRepository.save(restaurantTable);
    }

    @Override
    public List<RestaurantTable> findAll() {
        return restaurantTableRepository.findAll();
    }

    private void checkNameExistence(String name) {
        Optional<RestaurantTable> table = restaurantTableRepository.findByName(name);
        if(table.isPresent()) {
            throw new RestaurantTableExistsException("Restaurant table with the name " + name + " already exists in the database.");
        }
    }
}
