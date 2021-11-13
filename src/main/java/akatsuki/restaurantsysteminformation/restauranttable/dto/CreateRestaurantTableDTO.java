package akatsuki.restaurantsysteminformation.restauranttable.dto;

public class CreateRestaurantTableDTO {
    private String name;
    private String state;
    private String shape;

    public CreateRestaurantTableDTO(String name, String state, String shape) {
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
