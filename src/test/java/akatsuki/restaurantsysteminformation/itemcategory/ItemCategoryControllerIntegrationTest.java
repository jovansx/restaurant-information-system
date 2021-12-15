package akatsuki.restaurantsysteminformation.itemcategory;

import akatsuki.restaurantsysteminformation.dto.LoginDTO;
import akatsuki.restaurantsysteminformation.dto.TokenDTO;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemCategoryControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/item-category";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ItemCategoryService itemCategoryService;

    private HttpHeaders headers;

    @BeforeEach
    public void login() {
        if (headers == null) {
            ResponseEntity<TokenDTO> responseEntity =
                    restTemplate.postForEntity("/api/authenticate",
                            new LoginDTO("liamneeson", "liamneeson"),
                            TokenDTO.class);
            String accessToken = Objects.requireNonNull(responseEntity.getBody()).getToken();
            this.headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
        }
    }

    @Test
    public void getAll_Valid_ObjectsReturned() {
        ResponseEntity<ItemCategoryDTO[]> responseEntity = restTemplate.getForEntity(URL_PREFIX, ItemCategoryDTO[].class);
        List<ItemCategoryDTO> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(5, list.size());
    }

    @Test
    public void getAllDrinkCategories_Valid_ObjectsReturned() {
        ResponseEntity<ItemCategory[]> responseEntity =
                restTemplate.exchange(URL_PREFIX + "/drink", HttpMethod.GET, new HttpEntity<>(headers), ItemCategory[].class);
        List<ItemCategory> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(2, list.size());
    }

    @Test
    public void getAllDishCategories_Valid_ObjectsReturned() {
        ResponseEntity<ItemCategory[]> responseEntity =
                restTemplate.exchange(URL_PREFIX + "/dish", HttpMethod.GET, new HttpEntity<>(headers), ItemCategory[].class);
        List<ItemCategory> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(3, list.size());
    }

    @Test
    public void create_ValidDto_ObjectIsCreated() {
        int size = itemCategoryService.getAll().size();

        HttpEntity<ItemCategoryDTO> entity = new HttpEntity<>(new ItemCategoryDTO("chips", ItemType.DISH), headers);
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX, HttpMethod.POST, entity, String.class);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

        List<ItemCategory> categories = itemCategoryService.getAll();
        Assertions.assertEquals(size + 1, categories.size());
        Assertions.assertEquals("Chips", categories.get(categories.size() - 1).getName());

        itemCategoryService.delete(Long.parseLong(res.getBody()));
    }

    @Test
    public void create_NameAlreadyExist_ErrorConflict() {
        HttpEntity<ItemCategoryDTO> entity = new HttpEntity<>(new ItemCategoryDTO("meat", ItemType.DISH), headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.POST, entity, Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }


    @Test
    public void delete_ValidId_ObjectRemoved() {
        ItemCategory itemCategory = itemCategoryService.create(new ItemCategory("chips", ItemType.DISH));
        int size = itemCategoryService.getAll().size();

        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/" + itemCategory.getId(),
                HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(size - 1, itemCategoryService.getAll().size());
    }

    @Test
    public void delete_ObjectIsUsed_ErrorConflict() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/1",
                HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

}