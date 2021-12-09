package akatsuki.restaurantsysteminformation.itemcategory;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @Test
    public void testGetOne_ValidId_ObjectIsReturned() {
        ResponseEntity<ItemCategoryDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/1", ItemCategoryDTO.class);
        ItemCategoryDTO dto = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("Juices", dto.getName());
    }

    @Test
    public void testGetOne_IdDoesNotExist_ErrorNotFoundReturned() {
        ResponseEntity<ItemCategoryDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/44", ItemCategoryDTO.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getAll_Valid_ObjectsReturned() {
        ResponseEntity<ItemCategoryDTO[]> responseEntity = restTemplate.getForEntity(URL_PREFIX, ItemCategoryDTO[].class);
        List<ItemCategoryDTO> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(5, list.size());
    }

    @Test
    public void create_ValidDto_ObjectIsCreated() {
        int size = itemCategoryService.getAll().size();

        ResponseEntity<String> res = restTemplate.postForEntity(URL_PREFIX, new ItemCategoryDTO("chips", ItemType.DISH), String.class);

        Assertions.assertNotNull(res.getBody());

        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

        List<ItemCategory> categories = itemCategoryService.getAll();
        Assertions.assertEquals(size + 1, categories.size());
        Assertions.assertEquals("Chips", categories.get(categories.size() - 1).getName());

        itemCategoryService.delete(Long.parseLong(res.getBody()));
    }

    @Test
    public void create_NameAlreadyExist_ErrorConflict() {
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(URL_PREFIX, new ItemCategoryDTO("meat", ItemType.DISH), Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void update_ValidEntityAndId_SavedObject() {
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT,
                        new HttpEntity<>(new ItemCategoryDTO("Edited juices", ItemType.DRINK)),
                        Void.class);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        ItemCategory itemCategory = itemCategoryService.getOne(1L);
        Assertions.assertEquals("Edited juices", itemCategory.getName());

        itemCategory.setName("Juices");
        itemCategoryService.save(itemCategory);
    }

    @Test
    public void update_EntityNameIsTheSame_ExceptionThrown() {
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT,
                        new HttpEntity<>(new ItemCategoryDTO("juices", ItemType.DRINK)),
                        Void.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void update_EntityNameAlreadyExist_ExceptionThrown() {
        ResponseEntity<Void> res =
                restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT,
                        new HttpEntity<>(new ItemCategoryDTO("meat", ItemType.DISH)),
                        Void.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void delete_ValidId_ObjectRemoved() {
        ItemCategory itemCategory = itemCategoryService.create(new ItemCategory("chips", ItemType.DISH));
        int size = itemCategoryService.getAll().size();

        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/" + itemCategory.getId(),
                HttpMethod.DELETE, new HttpEntity<>(null), Void.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(size - 1, itemCategoryService.getAll().size());
    }

    @Test
    public void delete_ObjectIsUsed_ErrorConflict() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/1",
                HttpMethod.DELETE, new HttpEntity<>(null), Void.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

}