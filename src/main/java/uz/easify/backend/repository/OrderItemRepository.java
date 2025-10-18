package uz.easify.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import uz.easify.backend.domain.entity.OrderItem;

/**
 * Repository interface for OrderItem entity operations.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Find all items for an order.
     */
    List<OrderItem> findByOrderIdAndDeletedFalseOrderByCreatedAtAsc(Long orderId);

    /**
     * Find all items containing a specific product.
     */
    List<OrderItem> findByProductIdAndDeletedFalse(Long productId);

    /**
     * Get top selling products.
     */
    @Query("SELECT oi.product.id, oi.productName, SUM(oi.quantity) as totalSold " +
           "FROM OrderItem oi WHERE oi.deleted = false " +
           "GROUP BY oi.product.id, oi.productName " +
           "ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts();
}
