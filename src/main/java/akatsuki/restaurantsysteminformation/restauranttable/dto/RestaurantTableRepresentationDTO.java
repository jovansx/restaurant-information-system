package akatsuki.restaurantsysteminformation.restauranttable.dto;

import akatsuki.restaurantsysteminformation.enums.TableShape;
import akatsuki.restaurantsysteminformation.enums.TableState;
import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;

public class RestaurantTableRepresentationDTO {
    private long id;
    private String name;
    private TableState state;
    private TableShape shape;

    public RestaurantTableRepresentationDTO(RestaurantTable table) {
        this.id = table.getId();
        this.name = table.getName();
        this.state = table.getState();
        this.shape = table.getShape();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
