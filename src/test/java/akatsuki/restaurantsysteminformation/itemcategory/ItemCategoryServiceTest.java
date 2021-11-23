package akatsuki.restaurantsysteminformation.itemcategory;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemServiceImpl;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryDeleteException;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNameException;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ItemCategoryServiceTest {

    @InjectMocks
    ItemCategoryServiceImpl itemCategoryService;

    @Mock
    ItemCategoryRepository itemCategoryRepositoryMock;

    @Mock
    ItemServiceImpl itemServiceMock;

    @Test
    @DisplayName("When valid id is passed, required object is returned.")
    public void getOne_ValidId_ReturnedObject() {
        ItemCategory itemCategory = new ItemCategory(1L, "Dessert");

        Mockito.when(itemCategoryRepositoryMock.findById(1L)).thenReturn(Optional.of(itemCategory));
        ItemCategory foundItemCategory = itemCategoryService.getOne(1L);

        Assertions.assertEquals(foundItemCategory, itemCategory);
    }

    @Test
    @DisplayName("When invalid id is passed, exception should occur.")
    public void getOne_NegativeId_ExceptionThrown() {
        Mockito.when(itemCategoryRepositoryMock.findById(8000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ItemCategoryNotFoundException.class, () -> itemCategoryService.getOne(8000L));
    }

    @Test
    @DisplayName("When valid item category name is passed, required object is returned.")
    public void getByName_ValidItemCategoryName_ReturnedObject() {
        ItemCategory existingItemCategory = new ItemCategory(1L, "Dessert");
        Mockito.when(itemCategoryRepositoryMock.findByName("Dessert")).thenReturn(existingItemCategory);
        ItemCategory foundItemCategory = itemCategoryService.getByName("Dessert");
        Assertions.assertEquals(foundItemCategory, existingItemCategory);
    }

    @Test
    @DisplayName("When invalid item category name is passed, exception should occur.")
    public void getByName_InvalidItemCategoryName_ExceptionThrown() {
        Mockito.when(itemCategoryRepositoryMock.findByName("")).thenReturn(null);
        ItemCategory foundItemCategory = itemCategoryService.getByName("");
        assertNull(foundItemCategory);
    }

    @Test
    @DisplayName("When there are objects in the database, return list.")
    void getAll_ItemCategoriesExist_ReturnedList() {
        List<ItemCategory> list = Collections.singletonList(new ItemCategory(1L, "Dessert"));
        Mockito.when(itemCategoryRepositoryMock.findAll()).thenReturn(list);

        List<ItemCategory> foundList = itemCategoryService.getAll();
        Assertions.assertEquals(foundList, list);
    }

    @Test
    @DisplayName("When there are not objects in the database, exception should occur.")
    void getAll_ItemCategoriesDontExist_ReturnedNull() {
        Mockito.when(itemCategoryRepositoryMock.findAll()).thenReturn(null);
        List<ItemCategory> foundList = itemCategoryService.getAll();
        assertNull(foundList);
    }

    @Test
    @DisplayName("When valid entity is passed, new object is created.")
    public void create_ValidEntity_SavedObject() {
        ItemCategory itemCategory = new ItemCategory(1L, "DESSERT");
        List<ItemCategory> itemCategories = Collections.singletonList(new ItemCategory(2L, "Soup"));

        Mockito.when(itemCategoryRepositoryMock.findAll()).thenReturn(itemCategories);

        itemCategoryService.create(itemCategory);

        Assertions.assertEquals(itemCategory.getName(), "Dessert");
        Mockito.verify(itemCategoryRepositoryMock, Mockito.times(1)).save(Mockito.any(ItemCategory.class));
    }

    @Test
    @DisplayName("When invalid entity is passed, exception should occur.")
    public void create_InvalidEntity_ExceptionThrown() {
        ItemCategory itemCategory = new ItemCategory(1L, "SOUP");
        List<ItemCategory> itemCategories = Collections.singletonList(new ItemCategory(2L, "Soup"));

        Mockito.when(itemCategoryRepositoryMock.findAll()).thenReturn(itemCategories);

        Assertions.assertThrows(ItemCategoryNameException.class, () -> itemCategoryService.create(itemCategory));
    }


    @Test
    @DisplayName("When valid entity and id are passed, required object is changed.")
    public void update_ValidEntityAndId_SavedObject() {
        ItemCategory existingItemCategory = new ItemCategory(1L, "Chocolate desert");
        ItemCategory itemCategory = new ItemCategory(2L, "Dessert");

        Mockito.when(itemCategoryRepositoryMock.findById(1L)).thenReturn(Optional.of(existingItemCategory));
        Mockito.when(itemCategoryRepositoryMock.findByName("Dessert")).thenReturn(null);

        itemCategoryService.update(itemCategory, 1L);

        Assertions.assertEquals(existingItemCategory.getName(), "Dessert");
        Mockito.verify(itemCategoryRepositoryMock, Mockito.times(1)).save(existingItemCategory);
    }

    @Test
    @DisplayName("When entity name is the same, exception should occur.")
    public void update_EntityNameIsTheSame_ExceptionThrown() {
        ItemCategory existingItemCategory = new ItemCategory(1L, "Dessert");
        ItemCategory itemCategory = new ItemCategory(2L, "DESSERT  ");

        Mockito.when(itemCategoryRepositoryMock.findById(1L)).thenReturn(Optional.of(existingItemCategory));

        Assertions.assertThrows(ItemCategoryNameException.class, () -> itemCategoryService.update(itemCategory, 1L));
    }

    @Test
    @DisplayName("When entity name already exist, exception should occur.")
    public void update_EntityNameAlreadyExist_ExceptionThrown() {
        ItemCategory existingItemCategory = new ItemCategory(1L, "Barbeque");
        ItemCategory itemCategory = new ItemCategory(2L, "DESSERT  ");
        ItemCategory foundCategoryByName = new ItemCategory(3L, "Dessert");

        Mockito.when(itemCategoryRepositoryMock.findById(1L)).thenReturn(Optional.of(existingItemCategory));
        Mockito.when(itemCategoryRepositoryMock.findByName("Dessert")).thenReturn(foundCategoryByName);

        Assertions.assertThrows(ItemCategoryNameException.class, () -> itemCategoryService.update(itemCategory, 1L));
    }

    @Test
    @DisplayName("When valid id is passed, required object is deleted.")
    public void delete_ValidId_SavedObject() {
        ItemCategory existingItemCategory = new ItemCategory(1L, "Chocolate desert");

        Mockito.when(itemCategoryRepositoryMock.findById(1L)).thenReturn(Optional.of(existingItemCategory));
        Mockito.when(itemServiceMock.getAllActive()).thenReturn(new ArrayList<>());

        itemCategoryService.delete(1L);

        Mockito.verify(itemCategoryRepositoryMock, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("When id is passed and , exception should occur.")
    public void delete_ItemHasCategory_ExceptionThrown() {
        ItemCategory existingItemCategory = new ItemCategory(1L, "Dessert");
        List<Item> items = new ArrayList<>();
        items.add(new Item("Kinder", "Creamy", null, true, false, ItemType.DISH, null, existingItemCategory, null));

        Mockito.when(itemCategoryRepositoryMock.findById(1L)).thenReturn(Optional.of(existingItemCategory));
        Mockito.when(itemServiceMock.getAllActive()).thenReturn(items);

        Assertions.assertThrows(ItemCategoryDeleteException.class, () -> itemCategoryService.delete(1L));
    }

    @Test
    @DisplayName("When valid entity is passed, required object is saved.")
    public void save_ValidEntity_SavedObject() {
        ItemCategory itemCategory = new ItemCategory("Chocolate desert");
        itemCategoryService.save(itemCategory);
        Mockito.verify(itemCategoryRepositoryMock, Mockito.times(1)).save(itemCategory);
    }
}