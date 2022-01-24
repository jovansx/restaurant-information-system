package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.dto.LoginDTO;
import akatsuki.restaurantsysteminformation.dto.TokenDTO;
import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.item.dto.*;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategoryService;
import akatsuki.restaurantsysteminformation.itemcategory.dto.ItemCategoryDTO;
import akatsuki.restaurantsysteminformation.price.Price;
import akatsuki.restaurantsysteminformation.price.PriceService;
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
    public void getOne_ValidId_ObjectIsReturned() {
        ResponseEntity<ItemDetailsDTO> responseEntity =
                restTemplate.exchange(URL_PREFIX + "/not-active/1", HttpMethod.GET, new HttpEntity<>(headers), ItemDetailsDTO.class);
        ItemDetailsDTO dto = responseEntity.getBody();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(2, dto.getPrices().size());
        Assertions.assertEquals("c6d7d3c8-2273-4343-a6dc-87efe43867fa", dto.getCode());
        Assertions.assertEquals("Juices", dto.getItemCategory().getName());
    }

    @Test
    public void getOne_IdDoesNotExist_ErrorNotFoundReturned() {
        ResponseEntity<ItemDetailsDTO> responseEntity =
                restTemplate.exchange(URL_PREFIX + "/not-active/44", HttpMethod.GET, new HttpEntity<>(headers), ItemDetailsDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
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
    public void getAllForMenuInsight_Valid_ReturnedList() {
        ResponseEntity<ItemBasicInfoDTO[]> responseEntity =
                restTemplate.exchange(URL_PREFIX + "/menu", HttpMethod.GET, new HttpEntity<>(headers), ItemBasicInfoDTO[].class);
        List<ItemBasicInfoDTO> list = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(4, list.size());
    }

    @Test
    public void create_ValidDto_ObjectIsCreated() {
        int size = itemService.getAll().size();

        ItemCreateDTO createDTO = new ItemCreateDTO(new ItemCategoryDTO("Juices", ItemType.DRINK), 400, "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        HttpEntity<ItemCreateDTO> entity = new HttpEntity<>(createDTO, headers);
        ResponseEntity<String> res =
                restTemplate.exchange(URL_PREFIX, HttpMethod.POST, entity, String.class);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

        List<Item> items = itemService.getAll();
        Assertions.assertEquals(size + 1, items.size());
        Assertions.assertEquals("Lemon juice", items.get(items.size() - 1).getName());
        Assertions.assertEquals("It is made from lemon.", items.get(items.size() - 1).getDescription());

        itemService.deleteForTesting(Long.parseLong(res.getBody()));
    }

    @Test
    public void create_CategoryDoesNotExist_ErrorNotFount() {
        ItemCreateDTO createDTO = new ItemCreateDTO(new ItemCategoryDTO("Chocolate", ItemType.DISH), 400, "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        HttpEntity<ItemCreateDTO> entity = new HttpEntity<>(createDTO, headers);
        ResponseEntity<String> res =
                restTemplate.exchange(URL_PREFIX, HttpMethod.POST, entity, String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void create_InvalidName_ErrorNotFount() {
        ItemCreateDTO createDTO = new ItemCreateDTO(new ItemCategoryDTO("Chocolate", ItemType.DISH), 400, null, "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        HttpEntity<ItemCreateDTO> entity = new HttpEntity<>(createDTO, headers);
        ResponseEntity<String> res =
                restTemplate.exchange(URL_PREFIX, HttpMethod.POST, entity, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void update_OneInListAndOriginalIsTrue_SavedObject() {
        int size = itemService.getAll().size();

        ItemUpdateDTO updateDTO = new ItemUpdateDTO(new ItemCategoryDTO("Juices", ItemType.DRINK), 400, "c6d7d3c8-2273-4343-a6dc-87efe43867fa", "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT,
                new HttpEntity<>(updateDTO, headers), String.class);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        List<Item> items = itemService.getAll();
        Assertions.assertEquals(size + 1, items.size());
        Assertions.assertEquals("Lemon juice", items.get(items.size() - 1).getName());
        Assertions.assertEquals("It is made from lemon.", items.get(items.size() - 1).getDescription());

        itemService.deleteForTesting(Long.parseLong(res.getBody()));
    }

    @Test
    public void update_OneInListAndOriginalIsFalse_SavedObject() {
        ItemUpdateDTO updateDTO = new ItemUpdateDTO(new ItemCategoryDTO("Juices", ItemType.DRINK), 413, "a8b9aab8-f7dc-4966-a3eb-09eecb7fa9d9", "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX + "/3", HttpMethod.PUT,
                new HttpEntity<>(updateDTO, headers), String.class);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        Item item = itemService.getOne(3L);
        Assertions.assertEquals("Lemon juice", item.getName());
        Assertions.assertEquals("It is made from lemon.", item.getDescription());

        ItemCategory category = itemCategoryService.getByName("Cocktails");
        item.setName("Sex on the beach");
        item.setDescription("Very good cocktail!");
        item.setItemCategory(category);
        item.setComponents(List.of("Potato", "Sugar"));
        Price price = item.getPrices().remove(1);

        itemService.save(item);
        priceService.delete(price);
    }

    @Test
    public void update_MoreThenOneInList_SavedObject() {
        ItemUpdateDTO updateDTO = new ItemUpdateDTO(new ItemCategoryDTO("Juices", ItemType.DRINK), 15000, "9a191868-228d-4dbb-819f-ca615d29fefe", "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX + "/5", HttpMethod.PUT,
                new HttpEntity<>(updateDTO, headers), String.class);

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
                new HttpEntity<>(updateDTO, headers), String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void update_InvalidId_ExceptionThrown() {
        ItemUpdateDTO updateDTO = new ItemUpdateDTO(new ItemCategoryDTO("Juices", ItemType.DRINK), 400, "9a191868-228d-4dbb-819f-ca615d29fefe", "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX + "/44", HttpMethod.PUT,
                new HttpEntity<>(updateDTO, headers), String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void update_NotValidCode_ExceptionThrown() {
        ItemUpdateDTO updateDTO = new ItemUpdateDTO(new ItemCategoryDTO("Juices", ItemType.DRINK), 400, "9a191868-228d-4dbb-819f-ca615d29fddd", "Lemon juice", "It is made from lemon.", null, ItemType.DRINK, List.of("Lemon"));
        ResponseEntity<String> res = restTemplate.exchange(URL_PREFIX + "/5", HttpMethod.PUT,
                new HttpEntity<>(updateDTO, headers), String.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    public void delete_CopyDoesNotExist_ObjectRemoved() {
        int size = itemService.getAll().size();

        ResponseEntity<String> responseEntity = restTemplate.exchange(URL_PREFIX + "/1",
                HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(size + 1, itemService.getAll().size());

        itemService.deleteForTesting(Long.parseLong(Objects.requireNonNull(responseEntity.getBody())));
    }

    @Test
    public void delete_CopyExist_ObjectRemoved() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL_PREFIX + "/5",
                HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Item item = itemService.getOneById(6L);
        Assertions.assertTrue(item.isDeleted());

        item.setDeleted(false);
        itemService.save(item);
    }

}
