package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.item.exception.ItemAlreadyDeletedException;
import akatsuki.restaurantsysteminformation.item.exception.ItemCodeNotValidException;
import akatsuki.restaurantsysteminformation.item.exception.ItemExistsException;
import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import akatsuki.restaurantsysteminformation.itemcategory.ItemCategoryService;
import akatsuki.restaurantsysteminformation.itemcategory.exception.ItemCategoryNotFoundException;
import akatsuki.restaurantsysteminformation.price.Price;
import akatsuki.restaurantsysteminformation.price.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private ItemCategoryService itemCategoryService;
    private PriceService priceService;

    @Autowired
    public void setItemRepository(ItemRepository itemRepository, ItemCategoryService itemCategoryService, PriceService priceService) {
        this.itemRepository = itemRepository;
        this.itemCategoryService = itemCategoryService;
        this.priceService = priceService;
    }

    @Override
    public Item getOne(Long id) {
        return getOneWithAll(id);
    }

    @Override
    public Item getOneActive(Long id) {
        Item item = getOneWithAll(id);
        if (!item.isOriginal())
            throw new ItemNotFoundException("Item with the id " + id + " is not found in the database.");
        return item;
    }

    @Override
    public List<Item> getAllActive() {
        return itemRepository.findAllActiveIndexes().stream().map(this::getOneActive).collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllActiveByCategory(String categoryName) {
        ItemCategory itemCategory = itemCategoryService.getByName(itemCategoryService.firstLetterUppercase(categoryName));
        if (itemCategory == null)
            throw new ItemCategoryNotFoundException("Item category with the name " + categoryName.toLowerCase() + " not found in the database.");
        return itemRepository.findAllByItemCategoryAndOriginalIsTrueAndDeletedIsFalse(itemCategory);
    }

    @Transactional
    @Override
    public void saveChanges() {
        List<Item> itemCopies = itemRepository.findAllByOriginalIsFalse();
        for (Item item : itemCopies) {
            Optional<Item> originalItemOptional = itemRepository.findOneByCodeAndOriginalIsTrueAndDeletedIsFalse(item.getCode());
            if (originalItemOptional.isEmpty()) {
                item.setOriginal(true);
                itemRepository.save(item);
            } else
                assignItemFields(item, originalItemOptional.get());

        }
        itemRepository.removeAllByOriginalIsFalse();
    }

    @Transactional
    @Override
    public void discardChanges() {
        itemRepository.removeAllByOriginalIsFalse();
    }

    @Override
    public void create(Item item) {
        ItemCategory itemCategory = itemCategoryService.getByName(item.getItemCategory().getName());
        if (itemCategory != null)
            item.setItemCategory(itemCategory);
        else
            throw new ItemCategoryNotFoundException("Item category with the name " + item.getItemCategory().getName() + " not found in the database.");
        if (checkIfCodeAlreadyExist(item.getCode()))
            throw new ItemExistsException("Item with the code " + item.getCode() + " is already in the database.");
        priceService.save(item.getPrices().get(0));
        itemRepository.save(item);
    }

    @Override
    public void update(Item item, long id) {
        ItemCategory itemCategory = itemCategoryService.getByName(item.getItemCategory().getName());
        if (itemCategory != null)
            item.setItemCategory(itemCategory);
        else
            throw new ItemCategoryNotFoundException("Item category with the code " + item.getCode() + " not found in the database.");

        Optional<Item> itemOptional = itemRepository.findByIdAndOriginalIsTrueAndDeletedIsFalse(id);
        if (itemOptional.isEmpty())
            throw new ItemNotFoundException("Item with the id " + id + " is not found in the database.");
        Item foundItem = itemOptional.get();
        if (!foundItem.getCode().equals(item.getCode()))
            throw new ItemCodeNotValidException("Item cannot change its code.");

        List<Item> itemList = itemRepository.findAllByCode(foundItem.getCode());

        if (itemList.size() == 1) {
            Price price = item.getPrices().get(0);
            priceService.save(price);
            itemRepository.save(item);
        } else if (itemList.size() > 2)
            throw new ItemExistsException("Item with the code " + item.getCode() + " already have copy in the database.");

        for (Item i : itemList) {
            if (!i.isOriginal())
                assignItemFields(item, i);
        }
    }

    @Override
    public void delete(long id) {
        Item item = getOneActive(id);
        List<Item> itemList = itemRepository.findAllByCodeEvenDeleted(item.getCode());
        if (itemList.size() == 1) {
            Item copy = new Item(item);
            priceService.save(copy.getPrices().get(0));
            copy.setOriginal(false);
            copy.setDeleted(true);
            itemRepository.save(copy);
        } else if (itemList.size() == 2) {
            for (Item i : itemList) {
                if (!i.isOriginal()) {
                    if (!i.isDeleted()) {
                        i.setDeleted(true);
                        itemRepository.save(i);
                    } else
                        throw new ItemAlreadyDeletedException("Item with the id " + id + " is already deleted in the database.");
                }
            }
        } else
            throw new ItemAlreadyDeletedException("Item with the id " + id + " is already deleted in the database.");
    }

    @Override
    public double getCurrentPriceOfItem(Long itemId) {
        Item item = getOne(itemId);
        int index = item.getPrices().size() - 1;
        return item.getPrices().get(index).getValue();
    }

    private Item getOneWithAll(Long id) {
        Item item = itemRepository.findOneAndFetchItemCategoryAndPrices(id).orElseThrow(
                () -> new ItemNotFoundException("Item with the id " + id + " is not found in the database."));
        Optional<Item> item2 = itemRepository.findOneAndFetchComponents(id);
        if (item2.isEmpty())
            item.setComponents(new ArrayList<>());
        else
            item.setComponents(item2.get().getComponents());
        return item;
    }

    private void assignItemFields(Item copy, Item original) {
        if (copy.isDeleted())
            original.setDeleted(true);
        else {
            original.setName(copy.getName());
            original.setDescription(copy.getDescription());
            original.setIconBase64(copy.getIconBase64());
            original.setComponents(new ArrayList<>(copy.getComponents()));
            original.setItemCategory(copy.getItemCategory());
            Price p = copy.getPrices().get(0);
            if (p.getValue() != getCurrentPriceOfItem(original.getId())) {
                Price newPrice = new Price(p.getCreatedAt(), p.getValue());
                original.getPrices().add(newPrice);
                priceService.save(newPrice);
            }
        }
        itemRepository.save(original);
    }

    private boolean checkIfCodeAlreadyExist(String code) {
        List<Item> itemList = itemRepository.findAllByCode(code);
        return !itemList.isEmpty();
    }

}
