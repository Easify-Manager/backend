package uz.easify.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.easify.backend.domain.entity.*;
import uz.easify.backend.dto.request.ProductRequest;
import uz.easify.backend.dto.response.ProductResponse;
import uz.easify.backend.exception.ResourceAlreadyExistsException;
import uz.easify.backend.exception.ResourceNotFoundException;
import uz.easify.backend.repository.*;
import uz.easify.backend.service.FileStorageService;
import uz.easify.backend.service.ProductService;
import uz.easify.backend.service.mapper.ProductMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ProductService with comprehensive product management logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductImageRepository productImageRepository;
    private final FileStorageService fileStorageService;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product with SKU: {}", request.getSku());

        // Validate SKU uniqueness
        if (productRepository.existsBySkuAndDeletedFalse(request.getSku())) {
            throw new ResourceAlreadyExistsException("Product", "SKU", request.getSku());
        }

        // Build product entity
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .sku(request.getSku())
                .price(request.getPrice())
                .compareAtPrice(request.getCompareAtPrice())
                .cost(request.getCost())
                .weight(request.getWeight())
                .weightUnit(request.getWeightUnit())
                .active(request.getActive() != null ? request.getActive() : true)
                .featured(request.getFeatured() != null ? request.getFeatured() : false)
                .tags(request.getTags())
                .build();

        // Set category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndDeletedFalse(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            product.setCategory(category);
        }

        // Save product
        product = productRepository.save(product);

        // Create inventory record
        Inventory inventory = Inventory.builder()
                .product(product)
                .quantity(request.getQuantity() != null ? request.getQuantity() : 0)
                .reservedQuantity(0)
                .lowStockThreshold(request.getLowStockThreshold())
                .trackInventory(request.getTrackInventory() != null ? request.getTrackInventory() : true)
                .allowBackorder(request.getAllowBackorder() != null ? request.getAllowBackorder() : false)
                .build();
        
        inventory = inventoryRepository.save(inventory);
        product.setInventory(inventory);

        log.info("Product created successfully with ID: {}", product.getId());
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with ID: {}", id);

        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        // Validate SKU uniqueness if changed
        if (!product.getSku().equals(request.getSku()) &&
                productRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new ResourceAlreadyExistsException("Product", "SKU", request.getSku());
        }

        // Update product fields
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setCompareAtPrice(request.getCompareAtPrice());
        product.setCost(request.getCost());
        product.setWeight(request.getWeight());
        product.setWeightUnit(request.getWeightUnit());
        product.setActive(request.getActive() != null ? request.getActive() : product.getActive());
        product.setFeatured(request.getFeatured() != null ? request.getFeatured() : product.getFeatured());
        product.setTags(request.getTags());

        // Update category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndDeletedFalse(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        // Update inventory if it exists
        Inventory inventory = product.getInventory();
        if (inventory != null && request.getQuantity() != null) {
            inventory.setQuantity(request.getQuantity());
            inventory.setLowStockThreshold(request.getLowStockThreshold());
            inventory.setTrackInventory(request.getTrackInventory() != null ? 
                    request.getTrackInventory() : inventory.getTrackInventory());
            inventory.setAllowBackorder(request.getAllowBackorder() != null ? 
                    request.getAllowBackorder() : inventory.getAllowBackorder());
        }

        product = productRepository.save(product);
        log.info("Product updated successfully with ID: {}", id);
        
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductBySku(String sku) {
        Product product = productRepository.findBySkuAndDeletedFalse(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "SKU", sku));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findByDeletedFalse(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {
        return productRepository.findByActiveAndDeletedFalse(true)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        // Verify category exists
        categoryRepository.findByIdAndDeletedFalse(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
        
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getFeaturedProducts() {
        return productRepository.findFeaturedProducts()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String searchTerm, Pageable pageable) {
        return productRepository.searchProducts(searchTerm, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findLowStockProducts()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getOutOfStockProducts() {
        return productRepository.findOutOfStockProducts()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse addProductImage(Long productId, MultipartFile file, String altText, Boolean isPrimary) {
        log.info("Adding image to product ID: {}", productId);

        Product product = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        // Store file
        String filePath = fileStorageService.storeFile(file, "products");

        // If this is a primary image, unset other primary images
        if (isPrimary != null && isPrimary) {
            product.getImages().forEach(img -> img.setIsPrimary(false));
        }

        // Create product image entity
        ProductImage image = ProductImage.builder()
                .product(product)
                .fileName(file.getOriginalFilename())
                .filePath(filePath)
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .altText(altText)
                .displayOrder(product.getImages().size())
                .isPrimary(isPrimary != null ? isPrimary : product.getImages().isEmpty())
                .build();

        product.addImage(image);
        productRepository.save(product);

        log.info("Image added successfully to product ID: {}", productId);
        return productMapper.toResponse(product);
    }

    @Override
    public void deleteProductImage(Long productId, Long imageId) {
        log.info("Deleting image ID: {} from product ID: {}", imageId, productId);

        Product product = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        ProductImage image = product.getImages().stream()
                .filter(img -> img.getId().equals(imageId) && !img.getDeleted())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product Image", imageId));

        // Delete file from filesystem
        fileStorageService.deleteFile(image.getFilePath());

        // Remove image from product
        product.removeImage(image);
        productRepository.save(product);

        log.info("Image deleted successfully from product ID: {}", productId);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Soft deleting product with ID: {}", id);

        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        product.setDeleted(true);
        product.setActive(false);
        productRepository.save(product);

        log.info("Product soft deleted successfully with ID: {}", id);
    }
}
