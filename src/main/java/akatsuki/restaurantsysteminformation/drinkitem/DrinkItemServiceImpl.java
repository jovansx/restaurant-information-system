package akatsuki.restaurantsysteminformation.drinkitem;

import akatsuki.restaurantsysteminformation.drinkitem.dto.DrinkItemUpdateDTO;
import akatsuki.restaurantsysteminformation.drinkitem.exception.DrinkItemNotFoundException;
import akatsuki.restaurantsysteminformation.item.Item;
import akatsuki.restaurantsysteminformation.item.ItemRepository;
import akatsuki.restaurantsysteminformation.item.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrinkItemServiceImpl implements DrinkItemService {
    private final DrinkItemRepository drinkItemRepository;
    private final ItemRepository itemRepository;

    @Override
    public DrinkItem create(DrinkItemUpdateDTO drinkItemDTO) {
        Optional<Item> item = itemRepository.findById(drinkItemDTO.getItemId());
        if(item.isEmpty()) {
            throw new ItemNotFoundException("Item with the id " + drinkItemDTO.getItemId() + " is not found in the database.");
        }
        DrinkItem drinkItem = new DrinkItem(drinkItemDTO.getAmount(), item.get());
        return drinkItemRepository.save(drinkItem);
    }

    @Override
    public DrinkItem update(DrinkItemUpdateDTO drinkItemDTO, long id) {
        Optional<DrinkItem> drinkItemMaybe = drinkItemRepository.findById(id);
        if(drinkItemMaybe.isEmpty()) {
            throw new DrinkItemNotFoundException("Drink item with the id " + id + " is not found in the database.");
        }
        DrinkItem drinkItem = drinkItemMaybe.get();
        drinkItem.setAmount(drinkItemDTO.getAmount());
        return drinkItemRepository.save(drinkItem);
    }

    @Override
    public DrinkItem delete(DrinkItem drinkItem) {
        drinkItemRepository.delete(drinkItem);
        return drinkItem;
    }

    @Override
    public void delete(long id) {
        Optional<DrinkItem> drinkItemMaybe = drinkItemRepository.findById(id);
        if(drinkItemMaybe.isEmpty()) {
            throw new DrinkItemNotFoundException("Drink item with the id " + id + " is not found in the database.");
        }
        drinkItemRepository.delete(drinkItemMaybe.get());
    }

    @Override
    public DrinkItem findByIdAndFetchItem(long id) {
        Optional<DrinkItem> item = drinkItemRepository.findByIdAndFetchItem(id);
        List<DrinkItem> items = drinkItemRepository.findAll();
        if(item.isEmpty()) {
            throw new DrinkItemNotFoundException("Drink item with the id" + id + "not found in the database.");
        }
        return item.get();
    }

    @Override
    public DrinkItem getOne(long id) {
        Optional<DrinkItem> drinkItemMaybe = drinkItemRepository.findById(id);
        if(drinkItemMaybe.isEmpty()) {
            throw new DrinkItemNotFoundException("Drink item with the id " + id + " is not found in the database.");
        }
        return drinkItemMaybe.get();
    }


}
