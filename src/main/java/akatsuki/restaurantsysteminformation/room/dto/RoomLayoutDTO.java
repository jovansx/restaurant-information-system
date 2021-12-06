package akatsuki.restaurantsysteminformation.room.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@NoArgsConstructor
public class RoomLayoutDTO {
    @Min(1) @Max(10)
    private
    @Getter
    @Setter
    int rows;
    @Min(1) @Max(10)
    private
    @Getter
    @Setter
    int columns;
}
