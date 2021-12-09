package akatsuki.restaurantsysteminformation.drinkitem;

import akatsuki.restaurantsysteminformation.drinkitem.exception.DrinkItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrinkItemServiceImpl implements DrinkItemService {
    private final DrinkItemRepository drinkItemRepository;

    @Override
    public DrinkItem create(DrinkItem drinkItem) {
        return drinkItemRepository.save(drinkItem);
    }

    @Override
    public DrinkItem delete(DrinkItem drinkItem) {
        drinkItemRepository.delete(drinkItem);
        return drinkItem;
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


}
