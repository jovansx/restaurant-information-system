package akatsuki.restaurantsysteminformation.drinkitem;

import akatsuki.restaurantsysteminformation.drinkitems.DrinkItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrinkItemRepository extends JpaRepository<DrinkItem, Long> {

    @Query("select d from DrinkItem d join fetch d.item it where d.id = (:id)")
    Optional<DrinkItem> findByIdAndFetchItem(long id);
}
