package akatsuki.restaurantsysteminformation.restauranttable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    Optional<RestaurantTable> findByName(String name);

    @Query("select t from RestaurantTable t left join fetch t.activeOrder o where t.id = (:id)")
    Optional<RestaurantTable> findByIdAndFetchOrder(long id);

    @Query("select t from RestaurantTable t left join fetch t.activeOrder o where t.name = (:name)")
    Optional<RestaurantTable> findByNameAndFetchOrder(String name);
}