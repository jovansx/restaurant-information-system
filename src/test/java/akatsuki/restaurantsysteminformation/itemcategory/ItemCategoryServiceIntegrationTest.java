package akatsuki.restaurantsysteminformation.itemcategory;

import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryDeleteException;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNameException;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemCategoryServiceIntegrationTest {

    @Autowired
    ItemCategoryServiceImpl itemCategoryService;

    @Test
    public void getOne_ValidId_ReturnedObject() {
        ItemCategory foundItemCategory = itemCategoryService.getOne(1L);
        Assertions.assertEquals(foundItemCategory.getName(), "Juices");
    }

    @Test
    public void getOne_NegativeId_ExceptionThrown() {
        Assertions.assertThrows(ItemCategoryNotFoundException.class, () -> itemCategoryService.getOne(8000L));
    }

    @Test
    public void getByName_ValidItemCategoryName_ReturnedObject() {
        ItemCategory foundItemCategory = itemCategoryService.getByName("Juices");
        Assertions.assertEquals(foundItemCategory.getId(), 1);
    }

    @Test
    public void getByName_InvalidItemCategoryName_ExceptionThrown() {
        ItemCategory foundItemCategory = itemCategoryService.getByName("");
        assertNull(foundItemCategory);
    }

    @Test
    void getAll_ItemCategoriesExist_ReturnedList() {
        List<ItemCategory> foundList = itemCategoryService.getAll();
        Assertions.assertEquals(foundList.size(), 5);
    }

    @Test
    public void create_ValidEntity_SavedObject() {
        ItemCategory itemCategory = new ItemCategory("DESSERT", CategoryType.DISH);
        ItemCategory createdItemCategory = itemCategoryService.create(itemCategory);
        Assertions.assertNotNull(createdItemCategory);
    }

    @Test
    public void create_InvalidEntity_ExceptionThrown() {
        ItemCategory itemCategory = new ItemCategory("Sandwich", CategoryType.DISH);
        Assertions.assertThrows(ItemCategoryNameException.class, () -> itemCategoryService.create(itemCategory));
    }

    @Test
    public void update_ValidEntityAndId_SavedObject() {
        ItemCategory itemCategory = new ItemCategory("Dessert", CategoryType.DISH);
        ItemCategory updatedItemCategory = itemCategoryService.update(itemCategory, 1L);
        Assertions.assertEquals("Dessert", updatedItemCategory.getName());
    }

    @Test
    public void update_EntityNameIsTheSame_ExceptionThrown() {
        ItemCategory itemCategory = new ItemCategory("Juices  ", CategoryType.DRINK);
        Assertions.assertThrows(ItemCategoryNameException.class, () -> itemCategoryService.update(itemCategory, 1L));
    }

    @Test
    public void update_EntityNameAlreadyExist_ExceptionThrown() {
        ItemCategory itemCategory = new ItemCategory("Cocktails  ", CategoryType.DRINK);
        Assertions.assertThrows(ItemCategoryNameException.class, () -> itemCategoryService.update(itemCategory, 1L));
    }

    @Test
    public void delete_ValidId_SavedObject() {
        ItemCategory deletedItemCategory = itemCategoryService.delete(5L);
        Assertions.assertEquals("Soup", deletedItemCategory.getName());
    }

    @Test
    public void delete_ItemHasCategory_ExceptionThrown() {
        Assertions.assertThrows(ItemCategoryDeleteException.class, () -> itemCategoryService.delete(1L));
    }
}