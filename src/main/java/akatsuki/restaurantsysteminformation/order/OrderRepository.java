package akatsuki.restaurantsysteminformation.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o left join fetch o.waiter w where o.id = (:id)")
    Optional<Order> findOrderByIdFetchWaiter(long id);

    @Query("select o from Order o left join fetch o.waiter w")
    Optional<List<Order>> findAllFetchWaiter();
}
