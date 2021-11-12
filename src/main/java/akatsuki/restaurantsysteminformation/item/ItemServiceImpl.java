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
    public List<Item> getAll() {
        return itemRepository.findAllAndFetchAll();
    }

    @Override
    public Item getOne(Long id) {
        return itemRepository.findOneActiveAndFetchAll(id).orElseThrow(
                () -> new ItemNotFoundException("Item with the id " + id + " is not found in the database."));
    }

    @Transactional
    @Override
    public void saveChanges() {
        List<Item> itemCopies = itemRepository.findAllByCurrentlyActiveIsFalse();
        for (Item item : itemCopies) {
            Optional<Item> originalItemOptional = itemRepository.findOneByCodeAndCurrentlyActiveIsTrueAndDeletedIsFalse(item.getCode());
            if (originalItemOptional.isEmpty()) {
                item.setCurrentlyActive(true);
                itemRepository.save(item);
            } else {
                assignItemFields(item, originalItemOptional.get());
            }

        }
        itemRepository.removeAllByCurrentlyActiveIsFalse();
    }

    private void assignItemFields(Item item, Item i) {
        //TODO treba videti za update da doda samo novu cenu
        if (item.isDeleted()) {
            i.setDeleted(true);
        }
        else {
            i.setName(item.getName());
            i.setDescription(item.getDescription());
            i.setIconBase64(item.getIconBase64());
            i.setComponents(new ArrayList<>(item.getComponents()));
            i.setItemCategory(item.getItemCategory());
            i.setPrices(item.getPrices());
            i.setDeleted(item.isDeleted());
        }

        itemRepository.save(i);
    }

    @Transactional
    @Override
    public void discardChanges() {
        itemRepository.removeAllByCurrentlyActiveIsFalse();
    }

    @Override
    public void create(Item item) {
        ItemCategory itemCategory= itemCategoryService.findByName(item.getItemCategory().getName());
        if (itemCategory != null) {
            item.setItemCategory(itemCategory);
        } else {
            throw new ItemCategoryNotFoundException("Item category with the name " + item.getItemCategory().getName() + " not found in the database.");
        }
        if (checkIfCodeAlreadyExist(item.getCode())) {
            throw new ItemExistsException("Item with the code " + item.getCode() + " is already in the database.");
        }
        priceService.save(item.getPrices().get(0));
        itemRepository.save(item);
    }

    private boolean checkIfCodeAlreadyExist(String code) {
        List<Item> itemList = itemRepository.findAllByCode(code);
        return !itemList.isEmpty();
    }

    @Override
    public void update(Item item, long id) {
        ItemCategory itemCategory = itemCategoryService.findByName(item.getItemCategory().getName());
        if (itemCategory != null) {
            item.setItemCategory(itemCategory);
        } else {
            throw new ItemCategoryNotFoundException("Item category with the code " + item.getCode() + " not found in the database.");
        }

        Optional<Item> itemOptional = itemRepository.findByIdAndCurrentlyActiveIsTrueAndDeletedIsFalse(id);
        if (itemOptional.isEmpty()) {
            throw new ItemNotFoundException("Item with the id " + id + " is not found in the database.");
        }
        Item foundItem = itemOptional.get();
        if (!foundItem.getCode().equals(item.getCode()))
            throw new ItemCodeNotValidException("Item cannot change his code.");

        List<Item> itemList = itemRepository.findAllByCode(foundItem.getCode());

        if (itemList.size() == 1) {
            Price price = item.getPrices().get(0);
            priceService.save(price);
            item.setPrices(foundItem.getPrices());
            item.getPrices().add(price);
            itemRepository.save(item);
        } else if (itemList.size() > 2)
            throw new ItemExistsException("Item with the code " + item.getCode() + " already have copy in the database.");

        for (Item i: itemList) {
            if (!i.isCurrentlyActive()) {
                assignItemFields(item, i);
            }
        }
    }

    @Override
    public void delete(long id) {
        Optional<Item> itemOptional = itemRepository.findOneActiveAndFetchAll(id);
        if (itemOptional.isEmpty())
            throw new ItemNotFoundException("Item with the id " + id + " is not found in the database.");
        Item item = itemOptional.get();
        String code = item.getCode();

        List<Item> itemList = itemRepository.findAllByCodeEvenDeleted(code);

        if (itemList.size() == 1) {
            Item i = itemList.get(0);
            Item copy = new Item(i);
            priceService.save(copy.getPrices().get(0));
            copy.setCurrentlyActive(false);
            copy.setDeleted(true);
            itemRepository.save(copy);
        } else {
            throw new ItemAlreadyDeletedException("Item with the id " + id + " is already deleted in the database.");
        }

    }

}
