package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.enums.ItemType;
import akatsuki.restaurantsysteminformation.item.exception.ItemAlreadyDeletedException;
import akatsuki.restaurantsysteminformation.item.exception.ItemCodeNotValidException;
import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategoryService;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNotFoundException;
import akatsuki.restaurantsysteminformation.price.Price;
import akatsuki.restaurantsysteminformation.price.PriceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    ItemServiceImpl itemService;
    @Mock
    ItemRepository itemRepositoryMock;
    @Mock
    ItemCategoryService itemCategoryServiceMock;
    @Mock
    PriceService priceServiceMock;

    @Test
    void getOne_InvalidId_ExceptionThrown() {
        Mockito.when(itemRepositoryMock.findOneAndFetchItemCategoryAndPrices(8000L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.getOne(8000L));
    }

    @Test
    void getOne_ValidId_ReturnedObject() {
        Item item = new Item();
        Mockito.when(itemRepositoryMock.findOneAndFetchItemCategoryAndPrices(1L)).thenReturn(Optional.of(item));
        Item foundItem = itemService.getOne(1L);
        Assertions.assertEquals(item, foundItem);
        Assertions.assertEquals(foundItem.getComponents().size(), 0);
    }

    @Test
    void getOne_ValidIdWithComponents_ReturnedObjectWithComponents() {
        Item item = new Item();
        item.setComponents(Collections.singletonList("aca"));
        Mockito.when(itemRepositoryMock.findOneAndFetchItemCategoryAndPrices(1L)).thenReturn(Optional.of(item));
        Mockito.when(itemRepositoryMock.findOneAndFetchComponents(1L)).thenReturn(Optional.of(item));
        Item foundItem = itemService.getOne(1L);
        Assertions.assertEquals(item, foundItem);
        Assertions.assertEquals(foundItem.getComponents().size(), 1);
    }

    @Test
    void getOneActive_NotOriginalItem_ExceptionThrown() {
        Item item = new Item();
        item.setOriginal(false);
        Mockito.when(itemRepositoryMock.findOneAndFetchItemCategoryAndPrices(1L)).thenReturn(Optional.of(item));
        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.getOneActive(1L));
    }

    @Test
    void getOneActive_ValidId_ReturnedObject() {
        Item item = new Item();
        item.setOriginal(true);
        Mockito.when(itemRepositoryMock.findOneAndFetchItemCategoryAndPrices(1L)).thenReturn(Optional.of(item));
        Item foundItem = itemService.getOneActive(1L);
        Assertions.assertEquals(item, foundItem);
    }

    @Test
    void getAllActiveByCategory_ActiveItemsWithEqualCategoryExist_ReturnedList() {
        Item item1 = new Item();
        Item item2 = new Item();
        List<Item> itemList = Arrays.asList(item1, item2);

        ItemCategory itemCategory = new ItemCategory("Meat", ItemType.DISH);
        Mockito.when(itemCategoryServiceMock.firstLetterUppercase("meat")).thenReturn("Meat");
        Mockito.when(itemCategoryServiceMock.getByName("Meat")).thenReturn(itemCategory);

        Mockito.when(itemRepositoryMock.findAllByItemCategoryAndOriginalIsTrueAndDeletedIsFalse(itemCategory)).thenReturn(itemList);

        List<Item> foundList = itemService.getAllActiveByCategory("meat");
        Assertions.assertEquals(foundList.size(), 2);
        Assertions.assertTrue(foundList.contains(item1));
        Assertions.assertTrue(foundList.contains(item2));
    }

    @Test
    void getAllActiveByCategory_ActiveItemsWithEqualCategoryExist_ExceptionThrown() {
        Mockito.when(itemCategoryServiceMock.firstLetterUppercase("meat")).thenReturn("Meat");
        Mockito.when(itemCategoryServiceMock.getByName("Meat")).thenReturn(null);

        Assertions.assertThrows(ItemCategoryNotFoundException.class, () -> itemService.getAllActiveByCategory("meat"));
    }

    @Test
    void saveChanges_Adding_NewAddedAndCopiesDeleted() {
        Item item1 = new Item();
        List<Item> itemList = List.of(item1);
        Mockito.when(itemRepositoryMock.findAllByOriginalIsFalse()).thenReturn(itemList);
        Mockito.when(itemRepositoryMock.findOneByCodeAndOriginalIsTrueAndDeletedIsFalse(Mockito.any())).thenReturn(Optional.empty());

        itemService.saveChanges();

        Mockito.verify(itemRepositoryMock, Mockito.times(1)).save(item1);
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).removeAllByOriginalIsFalse();
    }

    @Test
    void saveChanges_RemovingAndUpdating_SomeRemovedSomeUpdatedAndCopiesDeleted() {
        Item item1 = new Item();
        item1.setDeleted(false);
        item1.setComponents(new ArrayList<>());
        item1.setPrices(Collections.singletonList(new Price(LocalDateTime.now(), 25)));
        Item item2 = new Item();
        item2.setDeleted(true);
        Item item3 = new Item();
        item3.setDeleted(false);
        item3.setComponents(new ArrayList<>());
        item3.setPrices(new ArrayList<>());
        item3.getPrices().add(new Price(LocalDateTime.now(), 22));
        List<Item> itemList = List.of(item1, item2, item3);
        Mockito.when(itemRepositoryMock.findAllByOriginalIsFalse()).thenReturn(itemList);
        Mockito.when(itemRepositoryMock.findOneByCodeAndOriginalIsTrueAndDeletedIsFalse(Mockito.any()))
                .thenReturn(Optional.of(item1))
                .thenReturn(Optional.of(item2))
                .thenReturn(Optional.of(item3));
        Mockito.when(itemRepositoryMock.findOneAndFetchItemCategoryAndPrices(Mockito.any()))
                .thenReturn(Optional.of(item1))
                .thenReturn(Optional.of(item1));

        itemService.saveChanges();

        Mockito.verify(priceServiceMock, Mockito.times(1)).save(Mockito.any(Price.class));
        Mockito.verify(itemRepositoryMock, Mockito.times(3)).save(Mockito.any(Item.class));
    }

    @Test
    void discardChanges__CopiesDeleted() {
        itemService.discardChanges();
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).removeAllByOriginalIsFalse();
    }

    @Test
    void create_CategoryNotExist_ExceptionThrown() {
        Item item = new Item();
        item.setItemCategory(new ItemCategory("Meat", ItemType.DISH));

        Mockito.when(itemCategoryServiceMock.getByName("Meat")).thenReturn(null);

        Assertions.assertThrows(ItemCategoryNotFoundException.class, () -> itemService.create(item));
    }

    @Test
    void create_ValidItem_ObjectIsCreated() {
        Item item = new Item();
        item.setPrices(Collections.singletonList(new Price(LocalDateTime.now(), 22)));
        ItemCategory itemCategory = new ItemCategory("Meat", ItemType.DISH);
        item.setItemCategory(itemCategory);

        Mockito.when(itemCategoryServiceMock.getByName("Meat")).thenReturn(new ItemCategory("Meat", ItemType.DISH));

        Item createdItem = itemService.create(item);
        Assertions.assertNotNull(createdItem);

        Mockito.verify(itemRepositoryMock, Mockito.times(1)).save(item);
        Mockito.verify(priceServiceMock, Mockito.times(1)).save(item.getPrices().get(0));
    }

    @Test
    void update_InvalidItemId_ExceptionThrown() {
        Item item = new Item();
        item.setItemCategory(new ItemCategory("Meat", ItemType.DISH));

        Mockito.when(itemCategoryServiceMock.getByName("Meat")).thenReturn(new ItemCategory("Meat", ItemType.DISH));
        Mockito.when(itemRepositoryMock.findByIdAndDeletedIsFalse(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.update(item, 1L));
    }

    @Test
    void update_InvalidItemCode_ExceptionThrown() {
        ItemCategory itemCategory = new ItemCategory("Meat", ItemType.DISH);
        Item item = new Item();
        item.setCode("code1");
        item.setItemCategory(itemCategory);
        Item item2 = new Item();
        item2.setCode("code2");
        item2.setItemCategory(itemCategory);


        Mockito.when(itemCategoryServiceMock.getByName("Meat")).thenReturn(new ItemCategory("Meat", ItemType.DISH));
        Mockito.when(itemRepositoryMock.findByIdAndDeletedIsFalse(1L)).thenReturn(Optional.of(item2));

        Assertions.assertThrows(ItemCodeNotValidException.class, () -> itemService.update(item, 1L));
    }

    @Test
    void update_FirstTimeValidUpdate_ObjectUpdated() {
        Item item = new Item();
        item.setCode("code1");
        item.setItemCategory(new ItemCategory("Meat", ItemType.DISH));
        item.setPrices(new ArrayList<>());
        item.getPrices().add(new Price(LocalDateTime.now(), 22));

        Item item2 = new Item();
        item2.setOriginal(true);

        Mockito.when(itemCategoryServiceMock.getByName("Meat")).thenReturn(new ItemCategory("Meat", ItemType.DISH));
        Mockito.when(itemRepositoryMock.findByIdAndDeletedIsFalse(1L)).thenReturn(Optional.of(item));
        Mockito.when(itemRepositoryMock.findAllByCodeAndPrices("code1")).thenReturn(List.of(item2));

        itemService.update(item, 1L);

        Mockito.verify(priceServiceMock, Mockito.times(1)).save(item.getPrices().get(0));
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).save(item);
    }

    @Test
    void update_SecondTimeValidUpdate_ObjectUpdated() {
        Item item = new Item();
        item.setCode("code1");
        item.setItemCategory(new ItemCategory("Meat", ItemType.DISH));
        item.setPrices(new ArrayList<>());
        item.getPrices().add(new Price(LocalDateTime.now(), 22));
        item.setComponents(new ArrayList<>());

        Item item2 = new Item();
        item2.setOriginal(true);
        Item item3 = new Item();
        item3.setOriginal(false);
        item3.setPrices(new ArrayList<>());
        item.getPrices().add(new Price(LocalDateTime.now(), 22));


        Mockito.when(itemCategoryServiceMock.getByName("Meat")).thenReturn(new ItemCategory("Meat", ItemType.DISH));
        Mockito.when(itemRepositoryMock.findByIdAndDeletedIsFalse(1L)).thenReturn(Optional.of(item));
        Mockito.when(itemRepositoryMock.findAllByCodeAndPrices("code1")).thenReturn(List.of(item2, item3));
        Mockito.when(itemRepositoryMock.findOneAndFetchItemCategoryAndPrices(Mockito.any())).thenReturn(Optional.of(item));

        itemService.update(item, 1L);

        Mockito.verify(itemRepositoryMock, Mockito.times(1)).save(Mockito.any(Item.class));
    }

    @Test
    void delete_FirstValidDelete_ItemIsDeleted() {
        Item item = new Item();
        item.setOriginal(true);
        item.setCode("code1");
        item.setItemCategory(new ItemCategory("Meat", ItemType.DISH));
        item.setPrices(new ArrayList<>());
        item.getPrices().add(new Price(LocalDateTime.now(), 22));

        Mockito.when(itemRepositoryMock.findOneAndFetchItemCategoryAndPrices(1L)).thenReturn(Optional.of(item));
        Mockito.when(itemRepositoryMock.findAllByCodeAndPrices("code1")).thenReturn(List.of(new Item()));

        itemService.delete(1L);

        Mockito.verify(priceServiceMock, Mockito.times(1)).save(Mockito.any(Price.class));
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).save(Mockito.any(Item.class));
    }

    @Test
    void delete_OneCopyAlreadyDeleted_ExceptionThrown() {
        Item item = new Item();
        item.setOriginal(true);
        item.setCode("code1");
        item.setItemCategory(new ItemCategory("Meat", ItemType.DISH));
        item.setPrices(new ArrayList<>());
        item.getPrices().add(new Price(LocalDateTime.now(), 22));

        Item item2 = new Item();
        item2.setOriginal(true);

        Item item3 = new Item();
        item3.setOriginal(false);
        item3.setDeleted(true);

        Mockito.when(itemRepositoryMock.findOneAndFetchItemCategoryAndPrices(1L)).thenReturn(Optional.of(item));
        Mockito.when(itemRepositoryMock.findAllByCodeAndPrices("code1")).thenReturn(List.of(item2, item3));

        Assertions.assertThrows(ItemAlreadyDeletedException.class, () -> itemService.delete(1L));
    }

    @Test
    void delete_OneValidCopy_ExceptionThrown() {
        Item item = new Item();
        item.setOriginal(true);
        item.setCode("code1");
        item.setItemCategory(new ItemCategory("Meat", ItemType.DISH));
        item.setPrices(new ArrayList<>());
        item.getPrices().add(new Price(LocalDateTime.now(), 22));

        Item item2 = new Item();
        item2.setOriginal(true);

        Item item3 = new Item();
        item3.setOriginal(false);
        item3.setDeleted(false);

        Mockito.when(itemRepositoryMock.findOneAndFetchItemCategoryAndPrices(1L)).thenReturn(Optional.of(item));
        Mockito.when(itemRepositoryMock.findAllByCodeAndPrices("code1")).thenReturn(List.of(item2, item3));

        itemService.delete(1L);

        Assertions.assertTrue(item3.isDeleted());
        Mockito.verify(itemRepositoryMock, Mockito.times(1)).save(Mockito.any(Item.class));
    }

}