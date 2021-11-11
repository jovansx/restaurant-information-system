package akatsuki.restaurantsysteminformation.room.dto;

public class RoomDTO {
    private String name;

    public RoomDTO(String name) {
        this.name = name;
    }

    public RoomDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
