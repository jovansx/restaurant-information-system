package akatsuki.restaurantsysteminformation.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("price")
public class PriceController {
    private final PriceServiceImpl priceService;

    //TODO CRUD
    @Autowired
    public PriceController(PriceServiceImpl priceService) {
        this.priceService = priceService;
    }
}
