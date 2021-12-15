package akatsuki.restaurantsysteminformation.itemcategory;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemServiceImpl;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryDeleteException;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNameException;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNotFoundException;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemCategoryServiceTest {

    @InjectMocks
    ItemCategoryServiceImpl itemCategoryService;

    @Mock
    ItemCategoryRepository itemCategoryRepositoryMock;

    @Mock
    ItemServiceImpl itemServiceMock;

    @Test
    public void getOne_ValidId_ReturnedObject() {
        ItemCategory itemCategory = new ItemCategory(1L, ItemType.DISH, "Dessert");

        Mockito.when(itemCategoryRepositoryMock.findById(1L)).thenReturn(Optional.of(itemCategory));
        ItemCategory foundItemCategory = itemCategoryService.getOne(1L);

        Assertions.assertEquals(foundItemCategory, itemCategory);
    }

    @Test
    public void getOne_InvalidId_ExceptionThrown() {
        Mockito.when(itemCategoryRepositoryMock.findById(8000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ItemCategoryNotFoundException.class, () -> itemCategoryService.getOne(8000L));
    }

    @Test
    public void getByName_ValidItemCategoryName_ReturnedObject() {
        ItemCategory existingItemCategory = new ItemCategory(1L, ItemType.DISH, "Dessert");
        Mockito.when(itemCategoryRepositoryMock.findByName("Dessert")).thenReturn(existingItemCategory);
        ItemCategory foundItemCategory = itemCategoryService.getByName("Dessert");
        Assertions.assertEquals(foundItemCategory, existingItemCategory);
    }

    @Test
    public void getByName_InvalidItemCategoryName_ExceptionThrown() {
        Mockito.when(itemCategoryRepositoryMock.findByName("")).thenReturn(null);
        ItemCategory foundItemCategory = itemCategoryService.getByName("");
        assertNull(foundItemCategory);
    }

    @Test
    void getAll_ItemCategoriesExist_ReturnedList() {
        List<ItemCategory> list = Collections.singletonList(new ItemCategory(1L, ItemType.DISH, "Dessert"));
        Mockito.when(itemCategoryRepositoryMock.findAll()).thenReturn(list);

        List<ItemCategory> foundList = itemCategoryService.getAll();
        Assertions.assertEquals(foundList, list);
    }

    @Test
    void getAll_ItemCategoriesDontExist_ReturnedEmpty() {
        Mockito.when(itemCategoryRepositoryMock.findAll()).thenReturn(new ArrayList<>());
        List<ItemCategory> foundList = itemCategoryService.getAll();
        assertNotNull(foundList);
        assertEquals(0, foundList.size());
    }

    @Test
    public void create_ValidEntity_SavedObject() {
        ItemCategory itemCategory = new ItemCategory(2L, ItemType.DISH, "   DESsERT    ");
        List<ItemCategory> itemCategories = Collections.singletonList(new ItemCategory(1L, ItemType.DISH, "Soup"));

        Mockito.when(itemCategoryRepositoryMock.findAll()).thenReturn(itemCategories);

        itemCategoryService.create(itemCategory);

        Assertions.assertEquals(itemCategory.getName(), "Dessert");
        Mockito.verify(itemCategoryRepositoryMock, Mockito.times(1)).save(Mockito.any(ItemCategory.class));
    }

    @Test
    public void create_NameAlreadyExists_ExceptionThrown() {
        ItemCategory itemCategory = new ItemCategory(2L, ItemType.DISH, "SOUP");
        List<ItemCategory> itemCategories = Collections.singletonList(new ItemCategory(1L, ItemType.DISH, "Soup"));

        Mockito.when(itemCategoryRepositoryMock.findAll()).thenReturn(itemCategories);

        Assertions.assertThrows(ItemCategoryNameException.class, () -> itemCategoryService.create(itemCategory));
    }

    @Test
    public void delete_ValidId_SavedObject() {
        ItemCategory existingItemCategory = new ItemCategory(1L, ItemType.DISH, "Chocolate desert");

        Mockito.when(itemCategoryRepositoryMock.findById(1L)).thenReturn(Optional.of(existingItemCategory));
        Mockito.when(itemServiceMock.getAllWithAll()).thenReturn(new ArrayList<>());

        itemCategoryService.delete(1L);

        Mockito.verify(itemCategoryRepositoryMock, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void delete_ItemHasCategory_ExceptionThrown() {
        ItemCategory existingItemCategory = new ItemCategory(1L, ItemType.DISH, "Dessert");
        List<Item> items = new ArrayList<>();
        items.add(new Item("Kinder", "Creamy", null, true, false, ItemType.DISH, null, existingItemCategory, null));

        Mockito.when(itemCategoryRepositoryMock.findById(1L)).thenReturn(Optional.of(existingItemCategory));
        Mockito.when(itemServiceMock.getAllWithAll()).thenReturn(items);

        Assertions.assertThrows(ItemCategoryDeleteException.class, () -> itemCategoryService.delete(1L));
    }

}