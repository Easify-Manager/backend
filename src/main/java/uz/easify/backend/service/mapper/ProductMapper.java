package uz.easify.backend.service.mapper;

import org.springframework.stereotype.Component;
import uz.easify.backend.domain.entity.*;
import uz.easify.backend.dto.response.*;
import uz.easify.backend.service.FileStorageService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting Product entities to DTOs.
 */
@Component
public class ProductMapper {

    private final FileStorageService fileStorageService;

    public ProductMapper(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * Convert Product entity to ProductResponse DTO.
     */
    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sku(product.getSku())
                .price(product.getPrice())
                .compareAtPrice(product.getCompareAtPrice())
                .cost(product.getCost())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .inventory(mapInventory(product.getInventory()))
                .images(mapImages(product.getImages()))
                .weight(product.getWeight())
                .weightUnit(product.getWeightUnit())
                .active(product.getActive())
                .featured(product.getFeatured())
                .tags(product.getTags())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * Map Inventory entity to InventoryResponse DTO.
     */
    private InventoryResponse mapInventory(Inventory inventory) {
        if (inventory == null) {
            return null;
        }

        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProduct().getId())
                .quantity(inventory.getQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .availableQuantity(inventory.getAvailableQuantity())
                .lowStockThreshold(inventory.getLowStockThreshold())
                .trackInventory(inventory.getTrackInventory())
                .allowBackorder(inventory.getAllowBackorder())
                .inStock(inventory.isInStock())
                .lowStock(inventory.isLowStock())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }

    /**
     * Map ProductImage entities to ProductImageResponse DTOs.
     */
    private List<ProductImageResponse> mapImages(List<ProductImage> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        return images.stream()
                .filter(img -> !img.getDeleted())
                .map(this::mapImage)
                .collect(Collectors.toList());
    }

    /**
     * Map ProductImage entity to ProductImageResponse DTO.
     */
    private ProductImageResponse mapImage(ProductImage image) {
        return ProductImageResponse.builder()
                .id(image.getId())
                .fileName(image.getFileName())
                .fileUrl(fileStorageService.getFileUrl(image.getFilePath()))
                .contentType(image.getContentType())
                .fileSize(image.getFileSize())
                .altText(image.getAltText())
                .displayOrder(image.getDisplayOrder())
                .isPrimary(image.getIsPrimary())
                .createdAt(image.getCreatedAt())
                .build();
    }
}
