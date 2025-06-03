package ch.martinelli.demo.nomocks.command;

import ch.martinelli.demo.nomocks.db.tables.records.OrderItemRecord;
import ch.martinelli.demo.nomocks.db.tables.records.PurchaseOrderRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static ch.martinelli.demo.nomocks.db.tables.Customer.CUSTOMER;
import static ch.martinelli.demo.nomocks.db.tables.OrderItem.ORDER_ITEM;
import static ch.martinelli.demo.nomocks.db.tables.Product.PRODUCT;
import static ch.martinelli.demo.nomocks.db.tables.PurchaseOrder.PURCHASE_ORDER;

@Service
class OrderService {

    private final DSLContext ctx;

    OrderService(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    PurchaseOrderRecord createOrder(long customerId) {
        var customer = ctx
                .selectFrom(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetchOptional()
                .orElseThrow(() -> new IllegalArgumentException("Customer does not exist"));

        var purchaseOrder = ctx.newRecord(PURCHASE_ORDER);
        purchaseOrder.setOrderDate(LocalDateTime.now());
        purchaseOrder.setCustomerId(customer.getId());

        purchaseOrder.store();

        return purchaseOrder;
    }

    @Transactional
    OrderItemRecord addItem(long purchaseOrderId, long productId, int quantity) {
        if (!ctx.fetchExists(ctx.selectFrom(PURCHASE_ORDER).where(PURCHASE_ORDER.ID.eq(purchaseOrderId)))) {
            throw new IllegalArgumentException("Purchase order does not exist");
        }

        if (!ctx.fetchExists(ctx.selectFrom(PRODUCT).where(PRODUCT.ID.eq(productId)))) {
            throw new IllegalArgumentException("Product does not exist");
        }

        var orderItem = ctx.newRecord(ORDER_ITEM);
        orderItem.setPurchaseOrderId(purchaseOrderId);
        orderItem.setProductId(productId);
        orderItem.setQuantity(quantity);
        orderItem.store();

        return orderItem;
    }

    @Transactional
    void updateQuantity(long orderItemId, int quantity) {
        int numRowsUpdated = ctx.update(ORDER_ITEM)
                .set(ORDER_ITEM.QUANTITY, quantity)
                .where(ORDER_ITEM.ID.eq(orderItemId))
                .execute();

        if (numRowsUpdated == 0) {
            throw new IllegalArgumentException("Order item does not exist");
        }
    }
}
