package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.order.dto.OrderDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/order";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getOneByRestaurantTableId_ValidId_ReturnedObject() {
        ResponseEntity<OrderDTO> res = restTemplate.getForEntity(URL_PREFIX + "/1" + "/1111", OrderDTO.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        Assertions.assertNotNull(res.getBody());
        OrderDTO orderDTO = res.getBody();
        Assertions.assertEquals(1, orderDTO.getId());
        Assertions.assertEquals(8.0, orderDTO.getTotalPrice());
        Assertions.assertEquals("2021-01-31 00:00:00", orderDTO.getCreatedAt());
        Assertions.assertEquals("John Cena", orderDTO.getWaiter());
        Assertions.assertEquals(5, orderDTO.getDishItemList().size());
        Assertions.assertEquals(5, orderDTO.getDrinkItemsList().size());
    }

    @Test
    public void getOneByRestaurantTableId_InvalidRestaurantTableId_ExceptionThrown() {
        ResponseEntity<OrderDTO> res = restTemplate.getForEntity(URL_PREFIX + "/8000" + "/1111", OrderDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void getOneByRestaurantTableId_InvalidPinCode_ExceptionThrown() {
        ResponseEntity<OrderDTO> res = restTemplate.getForEntity(URL_PREFIX + "/1" + "/7777", OrderDTO.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

}