package akatsuki.restaurantsysteminformation.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select distinct i from Item i join fetch i.itemCategory ic join fetch i.prices p where i.deleted = false")
    List<Item> findAllAndFetchAll();

    @Query("select distinct i from Item i join fetch i.itemCategory ic join fetch i.prices p where (:id) = i.id and i.original=true and i.deleted = false ")
    Optional<Item> findOneActiveAndFetchAll(Long id);

    @Query("select distinct i from Item i where (:code) = i.code")
    List<Item> findAllByCodeEvenDeleted(String code);

    List<Item> findAllByCode(String code);

    Optional<Item> findByIdAndOriginalIsTrueAndDeletedIsFalse(long id);

    void removeAllByOriginalIsFalse();

    List<Item> findAllByOriginalIsFalse();

    Optional<Item> findOneByCodeAndOriginalIsTrueAndDeletedIsFalse(String code);
}
