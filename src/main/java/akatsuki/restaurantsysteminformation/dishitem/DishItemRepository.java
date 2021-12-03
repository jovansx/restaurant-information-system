package akatsuki.restaurantsysteminformation.dishitem;

import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishItemRepository extends JpaRepository<DishItem, Long> {

    Optional<DishItem> findByIdAndActiveIsTrue(Long id);

    @Query("select d from DishItem d join fetch d.item i left join fetch d.chef b where d.id = (:id) and d.active = true " +
            "and not d.state = 4")
    Optional<DishItem> findOneActiveAndFetchItemAndChefAndStateIsNotDelivered(Long id);

    @Query("select d from DishItem d join fetch d.item i left join fetch d.chef b where d.id = (:id) and d.active = true")
    Optional<DishItem> findOneActiveAndFetchItemAndChef(Long id);

    List<DishItem> findAllByActiveIsTrueAndChef(UnregisteredUser chef);

    @Query("select distinct d from DishItem d join fetch d.item i left join fetch d.chef b where d.active = true " +
            "and ( d.state = 1 or d.state = 2 or d.state = 3 )")
    List<DishItem> findAllActiveAndFetchItemAndChefAndStateIsNotNewOrDelivered();

}