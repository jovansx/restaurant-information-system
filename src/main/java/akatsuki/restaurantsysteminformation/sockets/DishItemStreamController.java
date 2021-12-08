package akatsuki.restaurantsysteminformation.sockets;

import akatsuki.restaurantsysteminformation.dishitem.DishItem;
import akatsuki.restaurantsysteminformation.dishitem.DishItemService;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemActionRequestDTO;
import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemCreateDTO;
import akatsuki.restaurantsysteminformation.enums.ItemState;
import akatsuki.restaurantsysteminformation.sockets.dto.SocketResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequiredArgsConstructor
@Validated
public class DishItemStreamController {

    private final DishItemService dishItemService;
    private final SimpMessagingTemplate template;

    @MessageMapping({"/dish-item/create"})
    @SendTo("/topic/dish-item")
    public SocketResponseDTO create(@RequestBody @Valid DishItemCreateDTO dishItemCreateDTO) {
        dishItemService.create(dishItemCreateDTO);
        return new SocketResponseDTO(true, "Dish item is successfully created!");
    }

    @MessageMapping({"/dish-item/update/{id}"})
    @SendTo("/topic/dish-item")
    public SocketResponseDTO update(@RequestBody @Valid DishItemCreateDTO dishItemCreateDTO,
                                    @DestinationVariable @Positive(message = "Id has to be a positive value.") long id) {
        dishItemService.update(dishItemCreateDTO, id);
        return new SocketResponseDTO(true, "Dish item with " + id + " is successfully updated!");
    }

    @MessageMapping({"/dish-item/change-state"})
    @SendTo("/topic/dish-item")
    public SocketResponseDTO changeStateOfDishItem(@RequestBody @Valid DishItemActionRequestDTO dto) {
        DishItem dishItem = dishItemService.changeStateOfDishItems(dto.getItemId(), dto.getUserId());
        SocketResponseDTO socketResponseDTO = new SocketResponseDTO(true, "Dish item state is successfully changed!");
//        if (dishItem.getState().equals(ItemState.READY)) {
            this.template.convertAndSend("/topic/order", socketResponseDTO);
//        }
        return socketResponseDTO;
    }

    @MessageMapping({"/dish-item/delete/{id}"})
    @SendTo("/topic/dish-item")
    public SocketResponseDTO delete(@DestinationVariable @Positive(message = "Id has to be a positive value.") long id) {
        dishItemService.delete(id);
        return new SocketResponseDTO(true, "Dish item state is successfully deleted!");
    }

    @MessageExceptionHandler
    @SendTo("/topic/dish-item")
    public SocketResponseDTO handleException(RuntimeException exception) {
        return new SocketResponseDTO(false, exception.getLocalizedMessage());
    }
}
