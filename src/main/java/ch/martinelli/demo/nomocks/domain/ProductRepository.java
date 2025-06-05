package ch.martinelli.demo.nomocks.domain;

import ch.martinelli.demo.nomocks.db.tables.records.ProductPriceConfigurationRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static ch.martinelli.demo.nomocks.db.tables.Product.PRODUCT;
import static ch.martinelli.demo.nomocks.db.tables.ProductPriceConfiguration.PRODUCT_PRICE_CONFIGURATION;
import static org.jooq.Records.mapping;

@Repository
class ProductRepository {

    private final DSLContext ctx;

    ProductRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional(readOnly = true)
    public Optional<Product> findProduct(long productId) {
        return ctx
                .select(PRODUCT.ID, PRODUCT.NAME, PRODUCT.PRICE)
                .from(PRODUCT)
                .where(PRODUCT.ID.eq(productId))
                .fetchOptional(mapping(Product::new));
    }

    public Optional<ProductPriceConfigurationRecord> findProductPriceConfiguration(long productId) {
        return ctx
                .selectFrom(PRODUCT_PRICE_CONFIGURATION)
                .where(PRODUCT_PRICE_CONFIGURATION.PRODUCT_ID.eq(productId))
                .fetchOptional();
    }
}