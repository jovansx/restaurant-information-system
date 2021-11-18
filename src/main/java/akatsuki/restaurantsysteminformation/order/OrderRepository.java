package akatsuki.restaurantsysteminformation.order;

import akatsuki.restaurantsysteminformation.unregistereduser.UnregisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o left join fetch o.drinks dr join fetch o.waiter w where o.id = (:id)")
    Optional<Order> findByIdAndFetchWaiterAndDrinks(long id);

    @Query("select o from Order o left join fetch o.dishes dr left join fetch dr.item join fetch o.waiter w where o.id = (:id)")
    Optional<Order> findByIdAndFetchWaiterAndFetchDishesAndItems(long id);

    List<Order> findAllByActiveIsTrueAndWaiter(UnregisteredUser waiter);

    @Query("select o.id from Order o")
    List<Long> findAllIndexes();

    List<Order> findAllByCreatedAtBetween(LocalDateTime first, LocalDateTime second);
}
