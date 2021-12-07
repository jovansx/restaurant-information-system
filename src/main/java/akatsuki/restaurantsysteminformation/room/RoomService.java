package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.room.dto.RoomLayoutDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomTablesUpdateDTO;

import java.util.List;

public interface RoomService {
    Room getOne(long id);

    List<Room> getAll();

    Room create(Room room);

    Room update(Room room, long id);

    void updateName(String newName, long id);

    void updateLayout(RoomLayoutDTO layoutDTO, long id);

    Room delete(long id);

    Room updateByRoomDTO(RoomTablesUpdateDTO roomDTO, long id);

    void save(Room room);
}
