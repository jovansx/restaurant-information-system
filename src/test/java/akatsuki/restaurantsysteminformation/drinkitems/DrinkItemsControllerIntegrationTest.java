package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.drinkitems.dto.DrinkItemsDTO;
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
public class DrinkItemsControllerIntegrationTest {
    private static final String URL_PREFIX = "/api/drink-items";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getOneActive_ValidId_ObjectIsReturned() {
        ResponseEntity<DrinkItemsDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/active/5", DrinkItemsDTO.class);
        DrinkItemsDTO dto = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(2, dto.getItemList().size());
        Assertions.assertEquals("Apple juice", dto.getItemList().get(0).getItemName());
        Assertions.assertEquals("Orange juice", dto.getItemList().get(1).getItemName());
        Assertions.assertEquals(1, dto.getItemList().get(0).getAmount());
        Assertions.assertEquals(2, dto.getItemList().get(1).getAmount());
    }

    @Test
    public void getOneActive_IdDoesNotExist_ErrorNotFoundReturned() {
        ResponseEntity<DrinkItemsDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/44", DrinkItemsDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getAllActiveDrinkItems_Valid_ObjectsReturned() {
        ResponseEntity<ItemsActiveDTO[]> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/active", ItemsActiveDTO[].class);
        List<ItemsActiveDTO> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(4, list.size());
    }
}
