package ch.martinelli.demo.nomocks.query;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ch.martinelli.demo.nomocks.db.tables.OrderItem.ORDER_ITEM;
import static ch.martinelli.demo.nomocks.db.tables.PurchaseOrder.PURCHASE_ORDER;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.*;

@Repository
class OrderRepository {

    private final DSLContext ctx;

    OrderRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    List<PurchaseOrder> findOrders(String firstName, String lastName, int offset, int limit) {
        return ctx.select(PURCHASE_ORDER.ID,
                        PURCHASE_ORDER.ORDER_DATE,
                        row(PURCHASE_ORDER.customer().ID,
                                PURCHASE_ORDER.customer().FIRST_NAME,
                                PURCHASE_ORDER.customer().LAST_NAME,
                                PURCHASE_ORDER.customer().STREET,
                                PURCHASE_ORDER.customer().POSTAL_CODE,
                                PURCHASE_ORDER.customer().CITY
                        ).mapping(Customer::new),
                        multiset(
                                select(ORDER_ITEM.ID,
                                        ORDER_ITEM.QUANTITY,
                                        row(ORDER_ITEM.product().ID,
                                                ORDER_ITEM.product().NAME,
                                                ORDER_ITEM.product().PRICE
                                        ).mapping(Product::new))
                                        .from(ORDER_ITEM)
                                        .where(ORDER_ITEM.PURCHASE_ORDER_ID.eq(PURCHASE_ORDER.ID))
                                        .orderBy(ORDER_ITEM.ID)
                        ).convertFrom(r -> r.map(mapping(OrderItem::new))))
                .from(PURCHASE_ORDER)
                .where(PURCHASE_ORDER.customer().FIRST_NAME.likeIgnoreCase(firstName)
                        .or(PURCHASE_ORDER.customer().LAST_NAME.likeIgnoreCase(lastName)))
                .orderBy(PURCHASE_ORDER.ORDER_DATE)
                .offset(offset)
                .limit(limit)
                .fetch(mapping(PurchaseOrder::new));
    }
}
