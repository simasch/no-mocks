package ch.martinelli.demo.nomocks.domain;

import ch.martinelli.demo.nomocks.db.tables.records.OrderItemRecord;
import ch.martinelli.demo.nomocks.db.tables.records.PurchaseOrderRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static ch.martinelli.demo.nomocks.db.tables.OrderItem.ORDER_ITEM;
import static ch.martinelli.demo.nomocks.db.tables.PurchaseOrder.PURCHASE_ORDER;

@Repository
class OrderRepository {

    private final DSLContext ctx;

    OrderRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    PurchaseOrderRecord createOrder(long customerId) {
        var purchaseOrder = ctx.newRecord(PURCHASE_ORDER);
        purchaseOrder.setOrderDate(LocalDateTime.now());
        purchaseOrder.setCustomerId(customerId);

        purchaseOrder.store();

        return purchaseOrder;
    }

    @Transactional
    public OrderItemRecord addItem(long purchaseOrderId, long productId, int quantity, BigDecimal calculatedPrice) {
        var orderItem = new OrderItemRecord(null, quantity, calculatedPrice, purchaseOrderId, productId);
        ctx.attach(orderItem);
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

    @Transactional(readOnly = true)
    public boolean purchaseOrderExists(long purchaseOrderId) {
        return ctx.fetchExists(ctx.selectFrom(PURCHASE_ORDER).where(PURCHASE_ORDER.ID.eq(purchaseOrderId)));
    }

}