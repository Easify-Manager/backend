package uz.easify.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import uz.easify.backend.domain.entity.Inventory;

/**
 * Repository interface for Inventory entity operations.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Find inventory by product ID.
     */
    Optional<Inventory> findByProductIdAndDeletedFalse(Long productId);

    /**
     * Find all low stock inventory items.
     */
    @Query("SELECT i FROM Inventory i WHERE i.trackInventory = true " +
           "AND i.lowStockThreshold IS NOT NULL " +
           "AND (i.quantity - i.reservedQuantity) <= i.lowStockThreshold " +
           "AND i.deleted = false")
    List<Inventory> findLowStockInventory();

    /**
     * Find all out-of-stock inventory items.
     */
    @Query("SELECT i FROM Inventory i WHERE i.trackInventory = true " +
           "AND (i.quantity - i.reservedQuantity) <= 0 " +
           "AND i.deleted = false")
    List<Inventory> findOutOfStockInventory();
}
