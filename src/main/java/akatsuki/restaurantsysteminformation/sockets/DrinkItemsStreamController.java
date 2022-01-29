package akatsuki.restaurantsysteminformation.sockets;

import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import akatsuki.restaurantsysteminformation.drinkitems.DrinkItemsService;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsActionRequestDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsCreateDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsUpdateDTO;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Controller
@RequiredArgsConstructor
@Validated
public class DrinkItemsStreamController {

    private final DrinkItemsService drinkItemsService;
    private final SimpMessagingTemplate template;

    @MessageMapping({"/drink-items/create"})
    @SendTo("/topic/drink-items")
    public SocketResponseDTO create(@RequestBody @Valid DrinkItemsCreateDTO drinkItemsDTO) {
        DrinkItems drinkItems = drinkItemsService.create(drinkItemsDTO);
        SocketResponseDTO socketResponseDTO;
        if (drinkItemsDTO.getOrderCreateDTO() != null) {
            socketResponseDTO = new SocketResponseDTO(true, "Dish item is successfully created!", "ORDER_CREATED", drinkItems.getId());
        } else {
            socketResponseDTO = new SocketResponseDTO(true, "Dish item is successfully created!", "", drinkItems.getId());
        }
        this.template.convertAndSend("/topic/order", socketResponseDTO);
        return socketResponseDTO;
    }

    @MessageMapping({"/drink-items/update/{id}"})
    @SendTo("/topic/drink-items")
    public SocketResponseDTO update(@RequestBody DrinkItemsUpdateDTO drinkItemsDTO,
                                    @DestinationVariable @Min(value = 1, message = "Id has to be a positive value.") long id) {
        DrinkItems drinkItems = drinkItemsService.update(drinkItemsDTO, id);
        SocketResponseDTO socketResponseDTO = new SocketResponseDTO(true, "Drink items with " + id + " are successfully updated!", "", drinkItems.getId());
        this.template.convertAndSend("/topic/order", socketResponseDTO);
        return socketResponseDTO;
    }

    @MessageMapping({"/drink-items/change-state"})
    @SendTo("/topic/drink-items")
    public SocketResponseDTO changeStateOfDrinkItems(@RequestBody @Valid DrinkItemsActionRequestDTO dto) {
        drinkItemsService.changeStateOfDrinkItems(dto.getItemId(), dto.getUserId());
        SocketResponseDTO socketResponseDTO = new SocketResponseDTO(true, "Drink items state is successfully changed!", "", 0);
        this.template.convertAndSend("/topic/order", socketResponseDTO);
        return socketResponseDTO;
    }

    @MessageMapping({"/drink-items/delete/{id}"})
    @SendTo("/topic/drink-items")
    public SocketResponseDTO delete(@DestinationVariable @Positive(message = "Id has to be a positive value.") long id) {
        DrinkItems drinkItems = drinkItemsService.delete(id);
        SocketResponseDTO socketResponseDTO = new SocketResponseDTO(true, "Drink items with " + id + " are successfully deleted!", "", drinkItems.getId());
        this.template.convertAndSend("/topic/order", socketResponseDTO);
        return socketResponseDTO;
    }

    @MessageExceptionHandler
    @SendTo("/topic/drink-items")
    public SocketResponseDTO handleException(RuntimeException exception) {
        return new SocketResponseDTO(false, exception.getLocalizedMessage(), "", 0);
    }

}
