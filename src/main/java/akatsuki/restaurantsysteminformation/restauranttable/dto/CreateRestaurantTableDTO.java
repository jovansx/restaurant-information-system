package akatsuki.restaurantsysteminformation.restauranttable.dto;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;

public class CreateRestaurantTableDTO {
    private String name;
    private TableState state;
    private TableShape shape;

    public CreateRestaurantTableDTO(String name, TableState state, TableShape shape) {
        this.name = name;
        this.state = state;
        this.shape = shape;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TableState getState() {
        return state;
    }

    public void setState(TableState state) {
        this.state = state;
    }

    public TableShape getShape() {
        return shape;
    }

    public void setShape(TableShape shape) {
        this.shape = shape;
    }
}
