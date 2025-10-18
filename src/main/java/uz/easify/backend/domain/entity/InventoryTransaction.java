package uz.easify.backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.easify.backend.domain.enums.InventoryTransactionType;

/**
 * InventoryTransaction entity for tracking all inventory changes.
 * Provides an audit trail for stock movements.
 */
@Entity
@Table(name = "inventory_transactions", indexes = {
        @Index(name = "idx_inventory_transaction_product", columnList = "product_id"),
        @Index(name = "idx_inventory_transaction_type", columnList = "transaction_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 50)
    private InventoryTransactionType transactionType;

    @NotNull(message = "Quantity change is required")
    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;

    @NotNull(message = "Quantity after is required")
    @Column(name = "quantity_after", nullable = false)
    private Integer quantityAfter;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Column(name = "notes", length = 500)
    private String notes;

    @Size(max = 100, message = "Reference cannot exceed 100 characters")
    @Column(name = "reference", length = 100)
    private String reference;
}
