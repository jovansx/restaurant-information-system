package akatsuki.restaurantsysteminformation.drinkitems;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrinkItemsRepository extends JpaRepository<DrinkItems, Long> {
}
