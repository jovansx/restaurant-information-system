package akatsuki.restaurantsysteminformation.restauranttable.dto;

import akatsuki.restaurantsysteminformation.restauranttable.RestaurantTable;

public class RestaurantTableRepresentationDTO {
    private long id;
    private String name;
    private String state;
    private String shape;

    public RestaurantTableRepresentationDTO(long id, String name, String state, String shape) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.shape = shape;
    }

    public RestaurantTableRepresentationDTO(RestaurantTable table) {
        this.id = table.getId();
        this.name = table.getName();
        this.state = table.getState().toString();
        this.shape = table.getShape().toString();
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }
}
