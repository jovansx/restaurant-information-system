package akatsuki.restaurantsysteminformation.drinkitems;

import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrinkItemsRepository extends JpaRepository<DrinkItems, Long> {

    @Query("select distinct d from DrinkItems d join fetch d.drinkItemList dil join fetch d.bartender b join fetch dil.item i where d.active = true " +
            "and ( d.state = 2  or d.state = 3 )")
    List<DrinkItems> findAllNotOnHoldActive();

    @Query("select distinct d from DrinkItems d join fetch d.drinkItemList dil join fetch dil.item i where d.active = true " +
            "and d.state = 1 ")
    List<DrinkItems> findAllOnHoldActive();

    @Query("select d from DrinkItems d join fetch d.drinkItemList dil join fetch dil.item i left join fetch d.bartender b where d.id = (:id) and d.active = true " +
            "and ( d.state = 1 or d.state = 2  or d.state = 3 )")
    Optional<DrinkItems> findOneActiveWithBartenderAndWithItems(Long id);

    @Query("select d from DrinkItems d join fetch d.drinkItemList dil join fetch dil.item i left join fetch d.bartender b")
    Optional<List<DrinkItems>> findAllFetchBartender();

    List<DrinkItems> findAllByActiveIsTrueAndBartender(UnregisteredUser bartender);


}
