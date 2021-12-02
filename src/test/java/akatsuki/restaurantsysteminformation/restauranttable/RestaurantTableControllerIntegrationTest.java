package akatsuki.restaurantsysteminformation.restauranttable;

import akatsuki.restaurantsysteminformation.restauranttable.dto.RestaurantTableDTO;
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
class RestaurantTableControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/restaurant-table";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RestaurantTableService restaurantTableService;

    @Test
    public void getOne_InvalidId_ExceptionThrown() {
        ResponseEntity<RestaurantTableDTO> res = restTemplate.getForEntity(URL_PREFIX + "/8000", RestaurantTableDTO.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void getOne_ValidId_ObjectReturned() {
        ResponseEntity<RestaurantTableDTO> res = restTemplate.getForEntity(URL_PREFIX + "/1", RestaurantTableDTO.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        Assertions.assertNotNull(res.getBody());
        RestaurantTableDTO tableDTO = res.getBody();
        Assertions.assertEquals("T1", tableDTO.getName());
        Assertions.assertEquals("CIRCLE", tableDTO.getShape());
        Assertions.assertEquals("TAKEN", tableDTO.getState());
    }

    @Test
    public void getAll_FetchOrders_ReturnedList() {
        ResponseEntity<RestaurantTableDTO[]> res = restTemplate.getForEntity(URL_PREFIX, RestaurantTableDTO[].class);
        List<RestaurantTableDTO> list = Arrays.asList(Objects.requireNonNull(res.getBody()));
        Assertions.assertEquals(2, list.size());
    }

}