package uz.easify.backend.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.easify.backend.domain.enums.OrderStatus;

/**
 * Order entity representing customer orders/sales.
 * Stores complete order information including items, pricing, and status.
 */
@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_status", columnList = "status"),
        @Index(name = "idx_order_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Column(name = "customer_name", length = 200)
    private String customerName;

    @Column(name = "customer_email", length = 200)
    private String customerEmail;

    @Column(name = "customer_phone", length = 50)
    private String customerPhone;

    @Column(name = "customer_address", length = 500)
    private String customerAddress;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @NotNull(message = "Subtotal is required")
    @DecimalMin(value = "0.0", message = "Subtotal must be non-negative")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @DecimalMin(value = "0.0", message = "Tax must be non-negative")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "tax", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal tax = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Shipping must be non-negative")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "shipping", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal shipping = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Discount must be non-negative")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "discount", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @NotNull(message = "Total is required")
    @DecimalMin(value = "0.0", message = "Total must be non-negative")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "platform", length = 50)
    private String platform;  // e.g., "TELEGRAM", "INSTAGRAM", "WEB"

    @Column(name = "platform_user_id", length = 100)
    private String platformUserId;  // Customer's ID on the platform

    /**
     * Add an order item to this order.
     */
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    /**
     * Remove an order item from this order.
     */
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    /**
     * Calculate and update total based on items, tax, shipping, discount.
     */
    public void calculateTotal() {
        this.subtotal = items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.total = subtotal
                .add(tax != null ? tax : BigDecimal.ZERO)
                .add(shipping != null ? shipping : BigDecimal.ZERO)
                .subtract(discount != null ? discount : BigDecimal.ZERO);
    }
}
