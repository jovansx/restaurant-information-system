package akatsuki.restaurantsysteminformation.room;

import akatsuki.restaurantsysteminformation.room.dto.RoomLayoutDTO;
import akatsuki.restaurantsysteminformation.room.dto.RoomTablesUpdateDTO;

import java.util.List;

public interface RoomService {
    Room getOne(long id);

    List<Room> getAll();

    Room create(Room room);

    Room updateName(String newName, long id);

    Room updateLayout(RoomLayoutDTO layoutDTO, long id);

    Room updateTables(RoomTablesUpdateDTO roomDTO, long id);

    Room delete(long id);

    void save(Room room);
}
