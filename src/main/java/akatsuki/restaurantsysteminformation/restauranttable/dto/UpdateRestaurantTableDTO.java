package akatsuki.restaurantsysteminformation.restauranttable.dto;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;

public class UpdateRestaurantTableDTO extends CreateRestaurantTableDTO {
    private long id;

    public UpdateRestaurantTableDTO(String name, TableState state, TableShape shape, long id) {
        super(name, state, shape);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
