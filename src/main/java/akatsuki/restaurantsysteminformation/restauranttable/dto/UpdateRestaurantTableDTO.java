package akatsuki.restaurantsysteminformation.restauranttable.dto;

public class UpdateRestaurantTableDTO extends CreateRestaurantTableDTO {
    private long id;

    public UpdateRestaurantTableDTO(String name, String state, String shape, long id) {
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
