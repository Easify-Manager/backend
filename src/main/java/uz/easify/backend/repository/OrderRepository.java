package uz.easify.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import uz.easify.backend.domain.entity.Order;
import uz.easify.backend.domain.enums.OrderStatus;

/**
 * Repository interface for Order entity operations.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find order by ID excluding deleted ones.
     */
    Optional<Order> findByIdAndDeletedFalse(Long id);

    /**
     * Find order by order number.
     */
    Optional<Order> findByOrderNumberAndDeletedFalse(String orderNumber);

    /**
     * Find all non-deleted orders with pagination.
     */
    Page<Order> findByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Find orders by status.
     */
    List<Order> findByStatusAndDeletedFalseOrderByCreatedAtDesc(OrderStatus status);

    /**
     * Find orders by customer email.
     */
    List<Order> findByCustomerEmailAndDeletedFalseOrderByCreatedAtDesc(String customerEmail);

    /**
     * Find orders by platform and user ID.
     */
    List<Order> findByPlatformAndPlatformUserIdAndDeletedFalseOrderByCreatedAtDesc(
            String platform, String platformUserId);

    /**
     * Find orders within date range.
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate " +
           "AND o.deleted = false ORDER BY o.createdAt DESC")
    List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get total sales count.
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'DELIVERED' AND o.deleted = false")
    Long countCompletedOrders();

    /**
     * Check if order number exists.
     */
    boolean existsByOrderNumberAndDeletedFalse(String orderNumber);
}
