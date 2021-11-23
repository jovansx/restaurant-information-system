package akatsuki.restaurantsysteminformation.sockets.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class SocketResponseDTO {

    private boolean successfullyFinished;
    private String message;

}
