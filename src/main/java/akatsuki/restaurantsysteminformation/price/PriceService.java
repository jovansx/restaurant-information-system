package akatsuki.restaurantsysteminformation.price;

public interface PriceService {
    void save(Price price);

    void delete(Price price);

    void deleteById(long id);
}
