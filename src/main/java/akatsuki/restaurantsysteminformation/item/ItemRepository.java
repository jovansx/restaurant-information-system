package akatsuki.restaurantsysteminformation.item;

import akatsuki.restaurantsysteminformation.itemcategory.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findOneByCodeAndOriginalIsTrueAndDeletedIsFalse(String code);

    Optional<Item> findByIdAndDeletedIsFalse(long id);

    @Query("select distinct i from Item i join fetch i.itemCategory ic join fetch i.prices p where (:id) = i.id and i.deleted = false ")
    Optional<Item> findOneAndFetchItemCategoryAndPrices(Long id);

    @Query("select i from Item i left join fetch i.components ic where (:id) = i.id and i.deleted = false ")
    Optional<Item> findOneAndFetchComponents(Long id);

    @Query("select distinct i from Item i join fetch i.prices p where (:code) = i.code")
    List<Item> findAllByCodeAndPrices(String code);

    List<Item> findAllByOriginalIsFalse();

    List<Item> findAllByItemCategoryAndOriginalIsTrueAndDeletedIsFalse(ItemCategory itemCategory);

    @Query("select i.id from Item i where i.deleted = false")
    List<Long> findAllIndexes();

    void removeAllByOriginalIsFalse();
}
