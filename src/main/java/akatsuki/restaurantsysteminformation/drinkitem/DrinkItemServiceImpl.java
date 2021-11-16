package akatsuki.restaurantsysteminformation.drinkitem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrinkItemServiceImpl implements DrinkItemService {
    private final DrinkItemRepository drinkItemRepository;

    @Override
    public DrinkItem create(DrinkItem drinkItem) {
        return drinkItemRepository.save(drinkItem);
    }

    @Override
    public void delete(DrinkItem drinkItem) {
        drinkItemRepository.delete(drinkItem);
    }
}
