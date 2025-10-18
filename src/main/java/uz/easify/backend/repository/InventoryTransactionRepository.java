package uz.easify.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.easify.backend.domain.entity.InventoryTransaction;
import uz.easify.backend.domain.enums.InventoryTransactionType;

/**
 * Repository interface for InventoryTransaction entity operations.
 */
@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {

    /**
     * Find all transactions for a product with pagination.
     */
    Page<InventoryTransaction> findByProductIdAndDeletedFalseOrderByCreatedAtDesc(Long productId, Pageable pageable);

    /**
     * Find all transactions for a product.
     */
    List<InventoryTransaction> findByProductIdAndDeletedFalseOrderByCreatedAtDesc(Long productId);

    /**
     * Find transactions by type.
     */
    List<InventoryTransaction> findByTransactionTypeAndDeletedFalseOrderByCreatedAtDesc(InventoryTransactionType transactionType);

    /**
     * Find recent transactions across all products.
     */
    Page<InventoryTransaction> findByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
}
