package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.order.dto.OrderBasicInfoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/order";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getOneWithAll_InvalidId_ExceptionThrown() {
        ResponseEntity<OrderBasicInfoDTO> res = restTemplate.getForEntity(URL_PREFIX + "/1000", OrderBasicInfoDTO.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void getOneWithAll_ValidId_ObjectReturned() {
        ResponseEntity<OrderBasicInfoDTO> res = restTemplate.getForEntity(URL_PREFIX + "/1", OrderBasicInfoDTO.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        Assertions.assertNotNull(res.getBody());
        OrderBasicInfoDTO order = res.getBody();
        Assertions.assertEquals(8, order.getTotalPrice());
        Assertions.assertEquals(1, order.getWaiter().getId());
        Assertions.assertEquals(4, order.getDishItemList().size());
        Assertions.assertEquals(5, order.getDrinkItemsList().size());
    }

    @Test
    public void getOneByRestaurantTableId_ValidId_ReturnedObject() {
        ResponseEntity<OrderBasicInfoDTO> res = restTemplate.getForEntity(URL_PREFIX + "/1", OrderBasicInfoDTO.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        Assertions.assertNotNull(res.getBody());
        OrderBasicInfoDTO order = res.getBody();
        Assertions.assertEquals(1, order.getId());
    }

    @Test
    public void getAll_FetchOrders_ReturnedList() {
        ResponseEntity<OrderBasicInfoDTO[]> res = restTemplate.getForEntity(URL_PREFIX, OrderBasicInfoDTO[].class);
        List<OrderBasicInfoDTO> list = Arrays.asList(Objects.requireNonNull(res.getBody()));
        Assertions.assertEquals(10, list.size());
    }

    @Test
    public void getAllActive_FetchOrders_ReturnedList() {
        ResponseEntity<OrderBasicInfoDTO[]> res = restTemplate.getForEntity(URL_PREFIX + "/active", OrderBasicInfoDTO[].class);
        List<OrderBasicInfoDTO> list = Arrays.asList(Objects.requireNonNull(res.getBody()));
        Assertions.assertEquals(8, list.size());
    }

}