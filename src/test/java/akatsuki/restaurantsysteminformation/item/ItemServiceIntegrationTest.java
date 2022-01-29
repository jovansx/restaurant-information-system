package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.item.exception.ItemCodeNotValidException;
import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
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
    ItemService itemService;

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
    void getOne_InvalidId_ExceptionThrown() {
        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.getOne(8000L));
    }

    @Test
    void getOneActive_ValidId_ReturnedObject() {
        Item foundItem = itemService.getOneActive(1L);
        Assertions.assertNotNull(foundItem);
        Assertions.assertTrue(foundItem.isOriginal());
    }

    @Test
    void getOneActive_NotOriginalItem_ExceptionThrown() {
        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.getOneActive(6L));
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
    void getAllWithAll__ReturnedList() {
        List<Item> items = itemService.getAllWithAll();
        Assertions.assertEquals(6, items.size());
    }

    @Test
    void create_ValidItem_ObjectIsCreated() {
        Item item = new Item("coca cola", "", null, true, false, ItemType.DRINK,
                null, new ItemCategory("Meat", ItemType.DISH), null);
        item.setPrices(Collections.singletonList(new Price(LocalDateTime.now(), 22)));

        Item createdItem = itemService.create(item);
        Assertions.assertNotNull(createdItem);
    }

    @Test
    void create_CategoryNotExist_ExceptionThrown() {
        Item item = new Item();
        item.setItemCategory(new ItemCategory("Chips", ItemType.DISH));

        Assertions.assertThrows(ItemCategoryNotFoundException.class, () -> itemService.create(item));
    }

    @Test
    void update_OneInListAndOriginalIsTrue_ObjectUpdated() {
        Item item = new Item("coca cola", "", null, false, false, ItemType.DRINK,
                null, new ItemCategory("Juices", ItemType.DRINK), List.of(new Price(LocalDateTime.now(), 22)));
        item.setCode("c6d7d3c8-2273-4343-a6dc-87efe43867fa");

        Item updatedItem = itemService.update(item, 1L);
        Assertions.assertNotNull(updatedItem);
    }

    @Test
    void update_OneInListAndOriginalIsFalse_ObjectUpdated() {
        Item item = new Item("Milk", "", null, false, false, ItemType.DISH,
                new ArrayList<>(), new ItemCategory("Juices", ItemType.DRINK), List.of(new Price(LocalDateTime.now(), 22)));
        item.setCode("a8b9aab8-f7dc-4966-a3eb-09eecb7fa9d9");

        Item updatedItem = itemService.update(item, 3L);
        Assertions.assertNotNull(updatedItem);
    }

    @Test
    void update_MoreThenOneInList_ObjectUpdated() {
        Item item = new Item("Chicken meat", "", null, false, false, ItemType.DISH,
                new ArrayList<>(), new ItemCategory("Meat", ItemType.DISH), List.of(new Price(LocalDateTime.now(), 22)));
        item.setCode("9a191868-228d-4dbb-819f-ca615d29fefe");

        Item updatedItem = itemService.update(item, 5L);
        Assertions.assertNotNull(updatedItem);
    }

    @Test
    void update_InvalidItemId_ExceptionThrown() {
        Item item = new Item("coca cola", "", null, false, false, ItemType.DRINK,
                null, new ItemCategory("Meat", ItemType.DISH), null);

        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.update(item, 7L));
    }

    @Test
    void update_InvalidItemCode_ExceptionThrown() {
        Item item = new Item("coca cola", "", null, false, false, ItemType.DRINK,
                null, new ItemCategory("Meat", ItemType.DISH), null);
        item.setCode("c6d7d3c8");

        Assertions.assertThrows(ItemCodeNotValidException.class, () -> itemService.update(item, 1L));
    }

    @Test
    void getAllForMenuInsight_LessThenTwoItems_ReturnedList() {
        List<Item> items = itemService.getAllForMenuInsight();
        Assertions.assertEquals(4, items.size());
    }

    @Test
    void delete_FirstValidDelete_ItemIsDeleted() {
        Item deletedItem = itemService.delete(1L);
        Assertions.assertTrue(deletedItem.isDeleted());
    }

    @Test
    void delete_NotOriginalDelete_ItemIsDeleted() {
        Item deleted = itemService.delete(3L);
        Assertions.assertTrue(deleted.isDeleted());
    }

    @Test
    void delete_OneValidCopyAlreadyDeleted_ItemIsDeleted() {
        Item deletedItem = itemService.delete(5L);
        Assertions.assertNotNull(deletedItem);
    }

}