package akatsuki.restaurantsysteminformation.room.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateDTO {
    @NotEmpty(message = "It cannot be empty.")
    private @Getter
    @Setter
    String name;
}
