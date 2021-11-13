package akatsuki.restaurantsysteminformation.dishitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishItemRepository extends JpaRepository<DishItem, Long> {

    @Query("select distinct d from DishItem d join fetch d.chef b join fetch d.item i where d.active = true " +
            "and ( d.state = 2  or d.state = 3)")
    List<DishItem> findAllNotOnHoldActive();

    @Query("select distinct d from DishItem d join fetch d.item i where d.active = true " +
            "and d.state = 1")
    List<DishItem> findAllOnHoldActive();

    @Query("select d from DishItem d join fetch d.item i left join fetch d.chef b where d.id = (:id) and d.active = true " +
            "and ( d.state = 1 or d.state = 2  or d.state = 3 )")
    Optional<DishItem> findOneActiveWithChef(Long id);

    Optional<DishItem> findByIdAndActiveIsTrue(Long id);
}
