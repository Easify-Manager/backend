package uz.easify.backend.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Stub ProductSearchService used when Lucene dependencies are not available.
 * Keeps the bean present so other code can call it safely; methods are no-ops.
 */
@Service
@Slf4j
public class ProductSearchService {

    public record SearchResult(List<Long> ids, long totalHits) {}

    public void indexProduct(uz.easify.backend.domain.entity.Product product) {
        log.debug("ProductSearchService stub: indexProduct called for id={}", product != null ? product.getId() : null);
    }

    public void removeProduct(Long productId) {
        log.debug("ProductSearchService stub: removeProduct called for id={}", productId);
    }

    public SearchResult search(String queryString, Double minPrice, Double maxPrice, int offset, int limit) {
        log.debug("ProductSearchService stub: search called, returning empty result");
        return new SearchResult(List.of(), 0);
    }
}
