package akatsuki.restaurantsysteminformation.drinkitems;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DrinkItemsStreamController {

    @MessageMapping({"/hello"})
    @SendTo("/topic/greetings")
    public String greetings(String message) throws InterruptedException {
        Thread.sleep(1000);
        return "Hello";
    }
}
