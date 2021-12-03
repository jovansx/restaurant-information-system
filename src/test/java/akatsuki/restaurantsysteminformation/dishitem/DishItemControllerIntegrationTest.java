package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.dishitem.dto.DishItemDTO;
import akatsuki.restaurantsysteminformation.drinkitems.dto.ItemsActiveDTO;
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
public class DishItemControllerIntegrationTest {
    private static final String URL_PREFIX = "/api/dish-item";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getOneActive_ValidId_ObjectIsReturned() {
        ResponseEntity<DishItemDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/1", DishItemDTO.class);
        DishItemDTO dto = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1, dto.getItemList().size());
        Assertions.assertEquals("Chicken sandwich", dto.getItemList().get(0).getItemName());
        Assertions.assertEquals(1, dto.getItemList().get(0).getAmount());
    }

    @Test
    public void getOneActive_IdDoesNotExist_ErrorNotFoundReturned() {
        ResponseEntity<DishItemDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/44", DishItemDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void findAllActiveAndStateIsNotNewOrDelivered_Valid_ObjectsReturned() {
        ResponseEntity<ItemsActiveDTO[]> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/active", ItemsActiveDTO[].class);
        List<ItemsActiveDTO> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(3, list.size());
    }
}
