package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.item.exception.ItemAlreadyDeletedException;
import akatsuki.restaurantsysteminformation.item.exception.ItemCodeNotValidException;
import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
import akatsuki.restaurantsysteminformation.itemcategory.CategoryType;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNotFoundException;
import akatsuki.restaurantsysteminformation.price.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemServiceIntegrationTest {

    @Autowired
    ItemServiceImpl itemService;

    @Test
    void getOne_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.getOne(8000L));
    }

    @Test
    void getOne_ValidId_ReturnedObject() {
        Item foundItem = itemService.getOne(1L);
        Assertions.assertNotNull(foundItem);
    }

    @Test
    void getOne_ValidIdWithComponents_ReturnedObjectWithComponents() {
        Item foundItem = itemService.getOne(1L);
        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(2, foundItem.getComponents().size());
    }

    @Test
    void getOneActive_NotOriginalItem_ExceptionThrown() {
        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.getOneActive(6L));
    }

    @Test
    void getOneActive_ValidId_ReturnedObject() {
        Item foundItem = itemService.getOneActive(1L);
        Assertions.assertNotNull(foundItem);
        Assertions.assertTrue(foundItem.isOriginal());
    }

    @Test
    void getAllActive_ActiveItemsExist_ReturnedList() {
        List<Item> foundList = itemService.getAllActive();
        Assertions.assertEquals(5, foundList.size());
    }

    @Test
    void getAllActiveByCategory_ActiveItemsWithEqualCategoryExist_ReturnedList() {
        List<Item> foundList = itemService.getAllActiveByCategory("meat");
        Assertions.assertEquals(1, foundList.size());
    }

    @Test
    void getAllActiveByCategory_ActiveItemsWithEqualCategoryExist_ExceptionThrown() {
        Assertions.assertThrows(ItemCategoryNotFoundException.class, () -> itemService.getAllActiveByCategory("chips"));
    }

    @Test
    void create_CategoryNotExist_ExceptionThrown() {
        Item item = new Item();
        item.setItemCategory(new ItemCategory("Chips", CategoryType.DISH));

        Assertions.assertThrows(ItemCategoryNotFoundException.class, () -> itemService.create(item));
    }

    @Test
    void create_ValidItem_ObjectIsCreated() {
        Item item = new Item("coca cola", "", null, true, false, ItemType.DRINK,
                null, new ItemCategory("Meat", CategoryType.DISH), null);
        item.setPrices(Collections.singletonList(new Price(LocalDateTime.now(), 22)));

        Item createdItem = itemService.create(item);
        Assertions.assertNotNull(createdItem);

    }

    @Test
    void update_InvalidItemId_ExceptionThrown() {
        Item item = new Item("coca cola", "", null, true, false, ItemType.DRINK,
                null, new ItemCategory("Meat", CategoryType.DISH), null);

        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.update(item, 7L));
    }

    @Test
    void update_InvalidItemCode_ExceptionThrown() {
        Item item = new Item("coca cola", "", null, true, false, ItemType.DRINK,
                null, new ItemCategory("Meat", CategoryType.DISH), null);
        item.setCode("c6d7d3c8");

        Assertions.assertThrows(ItemCodeNotValidException.class, () -> itemService.update(item, 1L));
    }

    @Test
    void update_FirstTimeValidUpdate_ObjectUpdated() {
        Item item = new Item("coca cola", "", null, true, false, ItemType.DRINK,
                null, new ItemCategory("Juices", CategoryType.DRINK), List.of(new Price(LocalDateTime.now(), 22)));
        item.setCode("c6d7d3c8-2273-4343-a6dc-87efe43867fa");

        Item updatedItem = itemService.update(item, 1L);
        Assertions.assertNotNull(updatedItem);
    }

    @Test
    void update_SecondTimeValidUpdate_ObjectUpdated() {
        Item item = new Item("Chicken meat", "", null, true, false, ItemType.DISH,
                new ArrayList<>(), new ItemCategory("Meat", CategoryType.DISH), List.of(new Price(LocalDateTime.now(), 22)));
        item.setCode("9a191868-228d-4dbb-819f-ca615d29fefe");

        Item updatedItem = itemService.update(item, 5L);
        Assertions.assertNotNull(updatedItem);
    }

    @Test
    void delete_AlreadyDeleted_ExceptionThrown() {
        Item item = new Item("Chicken meat", "", null, true, false, ItemType.DISH,
                new ArrayList<>(), new ItemCategory("Meat", CategoryType.DISH), List.of(new Price(LocalDateTime.now(), 22)));
        item.setCode("7807ec36-1888-44a9-8fc5-ca11df02492f");

        Assertions.assertThrows(ItemAlreadyDeletedException.class, () -> itemService.delete(4L));
    }

    @Test
    void delete_FirstValidDelete_ItemIsDeleted() {
        Item item = new Item("Coca cola", "", null, true, false, ItemType.DRINK,
                new ArrayList<>(), new ItemCategory("Juices", CategoryType.DRINK), List.of(new Price(LocalDateTime.now(), 22)));
        item.setCode("c6d7d3c8-2273-4343-a6dc-87efe43867fa");

        Item deletedItem = itemService.delete(1L);
        Assertions.assertNotNull(deletedItem);
    }

    @Test
    void delete_OneValidCopy_ItemIsDeleted() {
        Item item = new Item("Chicken legs", "", null, true, false, ItemType.DISH,
                new ArrayList<>(), new ItemCategory("Meat", CategoryType.DISH), List.of(new Price(LocalDateTime.now(), 22)));
        item.setCode("9a191868-228d-4dbb-819f-ca615d29fefe");

        Item deletedItem = itemService.delete(5L);

        Assertions.assertNotNull(deletedItem);
    }
}