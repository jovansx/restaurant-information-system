package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsActionRequestDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.ItemsActiveDTO;
import akatsuki.restaurantsysteminformation.exceptions.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Validated
public class DrinkItemsStreamController {

    private final DrinkItemsService drinkItemsService;

    @MessageMapping({"/drink-items/change-state"})
    @SendTo("/topic/drink-items")
    public ItemsActiveDTO changeStateOfDrinkItems(@RequestBody @Valid DrinkItemsActionRequestDTO dto) {
        return new ItemsActiveDTO(drinkItemsService.changeStateOfDrinkItems(dto.getItemId(), dto.getUserId()));
    }

    @MessageExceptionHandler
    public ExceptionResponse handleException(RuntimeException exception) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getLocalizedMessage());
        return response;
    }
}
