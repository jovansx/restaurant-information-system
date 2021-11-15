package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableExistsException;
import akatsuki.restaurantsysteminformation.restauranttable.exception.RestaurantTableNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements RestaurantTableService {
    private final RestaurantTableRepository restaurantTableRepository;

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
        Optional<RestaurantTable> tableMaybe = restaurantTableRepository.findById(id);
        if (tableMaybe.isEmpty()) {
            throw new RestaurantTableNotFoundException("Restaurant table with the id " + id + " is not found in the database.");
        }
        checkNameExistence(restaurantTable.getName(), id);

        RestaurantTable table = tableMaybe.get();
        table.setName(restaurantTable.getName());
        table.setShape(restaurantTable.getShape());

        return restaurantTableRepository.save(table);
    }

    @Override
    public void delete(long id) {
        RestaurantTable table = deleteValidation(id);
        table.setDeleted(true);
        restaurantTableRepository.save(table);
    }

    @Override
    public RestaurantTable getOne(long id) {
        return restaurantTableRepository.findById(id).orElseThrow(
                () -> new RestaurantTableNotFoundException("Restaurant table the id " + id + " is not found in the database.")
        );
    }

    private RestaurantTable deleteValidation(long id) {
        Optional<RestaurantTable> tableMaybe = restaurantTableRepository.findById(id);
        if (tableMaybe.isEmpty()) {
            throw new RestaurantTableNotFoundException("Restaurant table with the id " + id + " is not found in the database.");
        }
        return tableMaybe.get();
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
