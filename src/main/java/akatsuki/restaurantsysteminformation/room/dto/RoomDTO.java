package akatsuki.restaurantsysteminformation.room.dto;

import akatsuki.restaurantsysteminformation.room.Room;

import java.util.List;

public class RoomDTO {
    private String name;
    private List<Long> restaurantTableIds;

    public RoomDTO(String name, List<Long> restaurantTableIds) {
        this.name = name;
        this.restaurantTableIds = restaurantTableIds;
    }

    public RoomDTO() {
    }

    public List<Long> getRestaurantTableIds() {
        return restaurantTableIds;
    }

    public void setRestaurantTableIds(List<Long> restaurantTableIds) {
        this.restaurantTableIds = restaurantTableIds;
    }

    public RoomDTO(Room room, List<Long> restaurantTableIds) {
        this.name = room.getName();
        this.restaurantTableIds = restaurantTableIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
