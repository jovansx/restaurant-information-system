package akatsuki.restaurantsysteminformation.price;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @InjectMocks
    PriceServiceImpl priceService;
    @Mock
    PriceRepository priceRepositoryMock;

    @Test
    @DisplayName("Object is saved.")
    void save__ObjectIsSaved() {
        priceService.save(new Price());
        Mockito.verify(priceRepositoryMock, Mockito.times(1)).save(Mockito.any(Price.class));
    }
}