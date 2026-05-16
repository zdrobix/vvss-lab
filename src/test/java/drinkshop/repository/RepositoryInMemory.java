package drinkshop.repository;

import drinkshop.domain.Product;

/**
 * In-memory implementation of Repository for Product entities.
 * Used for integration testing without file I/O.
 * 
 * Stores products in a HashMap (inherited from AbstractRepository).
 */
public class RepositoryInMemory extends AbstractRepository<Integer, Product> {

    /**
     * Extract the ID from a Product entity
     * 
     * @param entity the Product entity
     * @return the product ID
     */
    @Override
    protected Integer getId(Product entity) {
        return entity.getId();
    }
}
