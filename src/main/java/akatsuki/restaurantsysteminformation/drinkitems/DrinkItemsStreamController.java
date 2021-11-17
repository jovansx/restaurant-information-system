package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsActionRequestDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.ItemsActiveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Validated
public class DrinkItemsStreamController {

    private final DrinkItemsService drinkItemsService;

//    @MessageMapping({"/hello"})
//    @SendTo("/topic/drink-items")
//    public String greetings(String message) throws InterruptedException {
//        Thread.sleep(1000);
//        return "Hello";
//    }

    @MessageMapping({"/drink-items/change-state"})
    @SendTo("/topic/drink-items")
    public ItemsActiveDTO changeStateOfDrinkItems(@RequestBody @Valid DrinkItemsActionRequestDTO dto) {
        return new ItemsActiveDTO(drinkItemsService.changeStateOfDrinkItems(dto.getItemId(), dto.getUserId()));
    }
}
