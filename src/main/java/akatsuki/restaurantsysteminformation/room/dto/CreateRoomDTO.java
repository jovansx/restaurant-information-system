package akatsuki.restaurantsysteminformation.room.dto;

public class CreateRoomDTO {
    private String name;

    public CreateRoomDTO(String name) {
        this.name = name;
    }

    public CreateRoomDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
