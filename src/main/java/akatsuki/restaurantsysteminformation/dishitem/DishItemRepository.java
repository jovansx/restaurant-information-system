package akatsuki.restaurantsysteminformation.dishitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishItemRepository extends JpaRepository<DishItem, Long> {
}
