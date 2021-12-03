package akatsuki.restaurantsysteminformation.price;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {
    private final PriceRepository priceRepository;

    @Override
    public void save(Price price) {
        priceRepository.save(price);
    }

    @Override
    public void delete(Price price) {
        priceRepository.delete(price);
    }

    @Override
    public void deleteById(long id) {
        priceRepository.deleteById(id);
    }
}
