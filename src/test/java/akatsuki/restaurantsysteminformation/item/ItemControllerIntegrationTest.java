package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.item.dto.ItemCreateDTO;
import akatsuki.restaurantsysteminformation.item.dto.ItemDetailsDTO;
import akatsuki.restaurantsysteminformation.item.dto.ItemForMenuDTO;
import akatsuki.restaurantsysteminformation.item.dto.ItemUpdateDTO;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategoryService;
import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import akatsuki.restaurantsysteminformation.price.Price;
import akatsuki.restaurantsysteminformation.price.PriceService;
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
public class ItemControllerIntegrationTest {
    private static final String URL_PREFIX = "/api/item";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemCategoryService itemCategoryService;

    @Autowired
    private PriceService priceService;

    @Test
    public void getOneActive_ValidId_ObjectIsReturned() {
        ResponseEntity<ItemDetailsDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/1", ItemDetailsDTO.class);
        ItemDetailsDTO dto = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(2, dto.getPrices().size());
        Assertions.assertEquals("c6d7d3c8-2273-4343-a6dc-87efe43867fa", dto.getCode());
        Assertions.assertEquals("Juices", dto.getItemCategory().getName());
    }

    @Test
    public void getOneActive_IdDoesNotExist_ErrorNotFoundReturned() {
        ResponseEntity<ItemDetailsDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/44", ItemDetailsDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getAllActive_Valid_ObjectsReturned() {
        ResponseEntity<ItemDetailsDTO[]> responseEntity = restTemplate.getForEntity(URL_PREFIX, ItemDetailsDTO[].class);
        List<ItemDetailsDTO> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
        Assertions.assertEquals(5, list.size());
    }

    @Test
    public void getAllByCategory_Valid_ObjectsReturned() {
        ResponseEntity<ItemForMenuDTO[]> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/category/juices", ItemForMenuDTO[].class);
        List<ItemForMenuDTO> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("Apple juice", list.get(0).getName());
        Assertions.assertEquals("Orange juice", list.get(1).getName());
    }

    @Test
    public void getAllByCategory_CategoryDoesNotExist_ErrorNotFoundReturned() {
        ResponseEntity<ItemForMenuDTO> responseEntity = restTemplate.getForEntity(URL_PREFIX + "/category/soups", ItemForMenuDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void create_ValidDto_ObjectIsCreated() {
        int size = itemService.getAllWithAll().size();

        ItemCreateDTO createDTO = new ItemCreateDTO(new ItemCategoryDTO("Juices", ItemType.DRINK), 400, "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.postForEntity(URL_PREFIX, createDTO, String.class);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

        List<Item> items = itemService.getAllWithAll();
        Assertions.assertEquals(size + 1, items.size());
        Assertions.assertEquals("Lemon juice", items.get(items.size() - 1).getName());
        Assertions.assertEquals("It is made from lemon.", items.get(items.size() - 1).getDescription());

        itemService.deleteForTesting(Long.parseLong(res.getBody()));
    }

    @Test
    public void create_CategoryDoesNotExist_ErrorNotFount() {
        ItemCreateDTO createDTO = new ItemCreateDTO(new ItemCategoryDTO("Chocolate", ItemType.DISH), 400, "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.postForEntity(URL_PREFIX, createDTO, String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void create_InvalidName_ErrorNotFount() {
        ItemCreateDTO createDTO = new ItemCreateDTO(new ItemCategoryDTO("Chocolate", ItemType.DISH), 400, null, "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.postForEntity(URL_PREFIX, createDTO, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void update_ValidEntityAndIdFirst_SavedObject() {
        int size = itemService.getAllWithAll().size();

        ItemUpdateDTO updateDTO = new ItemUpdateDTO(new ItemCategoryDTO("Juices", ItemType.DRINK), 400, "c6d7d3c8-2273-4343-a6dc-87efe43867fa", "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT,
                new HttpEntity<>(updateDTO), String.class);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        List<Item> items = itemService.getAllWithAll();
        Assertions.assertEquals(size + 1, items.size());
        Assertions.assertEquals("Lemon juice", items.get(items.size() - 1).getName());
        Assertions.assertEquals("It is made from lemon.", items.get(items.size() - 1).getDescription());

        itemService.deleteForTesting(Long.parseLong(res.getBody()));
    }

    @Test
    public void update_ValidEntityAndIdSecond_SavedObject() {
        ItemUpdateDTO updateDTO = new ItemUpdateDTO(new ItemCategoryDTO("Juices", ItemType.DRINK), 400, "9a191868-228d-4dbb-819f-ca615d29fefe", "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX + "/5", HttpMethod.PUT,
                new HttpEntity<>(updateDTO), String.class);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        Item item = itemService.getOne(6L);
        Assertions.assertEquals("Lemon juice", item.getName());
        Assertions.assertEquals("It is made from lemon.", item.getDescription());

        ItemCategory category = itemCategoryService.getByName("Meat");
        item.setName("Chicken breast");
        item.setDescription("Solid chicken breast");
        item.setItemCategory(category);
        item.setIconBase64(null);
        item.setComponents(List.of("Chicken", "Potato"));
        Price price = item.getPrices().remove(1);

        itemService.save(item);
        priceService.delete(price);
    }

    @Test
    public void update_CategoryDoesNotExist_ExceptionThrown() {
        ItemUpdateDTO updateDTO = new ItemUpdateDTO(new ItemCategoryDTO("Chocolate", ItemType.DISH), 400, "9a191868-228d-4dbb-819f-ca615d29fefe", "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX + "/5", HttpMethod.PUT,
                new HttpEntity<>(updateDTO), String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void update_NotOriginalItem_ExceptionThrown() {
        ItemUpdateDTO updateDTO = new ItemUpdateDTO(new ItemCategoryDTO("Juices", ItemType.DRINK), 400, "9a191868-228d-4dbb-819f-ca615d29fefe", "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX + "/6", HttpMethod.PUT,
                new HttpEntity<>(updateDTO), String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void update_NotValidCode_ExceptionThrown() {
        ItemUpdateDTO updateDTO = new ItemUpdateDTO(new ItemCategoryDTO("Juices", ItemType.DRINK), 400, "9a191868-228d-4dbb-819f-ca615d29fddd", "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX + "/5", HttpMethod.PUT,
                new HttpEntity<>(updateDTO), String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void delete_CopyDoesNotExist_ObjectRemoved() {
        int size = itemService.getAllWithAll().size();

        ResponseEntity<String> responseEntity = restTemplate.exchange(URL_PREFIX + "/1",
                HttpMethod.DELETE, new HttpEntity<>(null), String.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(size + 1, itemService.getAllWithAll().size());

        itemService.deleteForTesting(Long.parseLong(Objects.requireNonNull(responseEntity.getBody())));
    }

    @Test
    public void delete_CopyExist_ObjectRemoved() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL_PREFIX + "/5",
                HttpMethod.DELETE, new HttpEntity<>(null), String.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Item item = itemService.getOneById(6L);
        Assertions.assertTrue(item.isDeleted());

        item.setDeleted(false);
        itemService.save(item);
    }

    @Test
    public void delete_CopyAlreadyDeleted_ExceptionThrown() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL_PREFIX + "/4",
                HttpMethod.DELETE, new HttpEntity<>(null), String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }
}
