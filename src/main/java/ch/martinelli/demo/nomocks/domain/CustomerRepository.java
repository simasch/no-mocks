package ch.martinelli.demo.nomocks.domain;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static ch.martinelli.demo.nomocks.db.tables.Customer.CUSTOMER;
import static org.jooq.Records.mapping;

@Repository
class CustomerRepository {

    private final DSLContext ctx;

    CustomerRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    Optional<Customer> findCustomer(long customerId) {
        return ctx
                .select(CUSTOMER.ID, CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME,
                        CUSTOMER.STREET, CUSTOMER.POSTAL_CODE, CUSTOMER.CITY)
                .from(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetchOptional(mapping(Customer::new));
    }
}
