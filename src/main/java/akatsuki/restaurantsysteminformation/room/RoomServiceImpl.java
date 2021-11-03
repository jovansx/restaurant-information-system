package akatsuki.restaurantsysteminformation.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;

    @Autowired
    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
}
