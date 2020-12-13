package at.htl.smallwind.entity;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * The sequence starts with 10248 because the minimum id in ordering.csv is 10248
 */
@Entity
@Table(name = "SW_ORDERING")
@SequenceGenerator(name = "OrderingSeq", initialValue = 10248, allocationSize = 1, sequenceName = "SW_ORDERING_SEQ")
@NamedQueries({
        @NamedQuery(
                name = "Ordering.findAll",
                query = "select o from Ordering o order by o.orderDate desc"
        )
})
public class Ordering {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OrderingSeq")
    @Column(name = "ORD_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ORD_CUST_ID")
    private Customer customer;

    @Column(name = "ORD_ORDER_DATE")
    private LocalDate orderDate;
    @Column(name = "ORD_REQUIRED_DATE")
    private LocalDate requiredDate;
    @Column(name = "ORD_SHIPPED_DATE")
    private LocalDate shippedDate;
    @Column(name = "ORD_FREIGHT")
    private double freight;

    public Ordering() {
    }

    public Ordering(Customer customer,
                    LocalDate orderDate,
                    LocalDate requiredDate,
                    LocalDate shippedDate,
                    double freight) {
        this.customer = customer;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.freight = freight;
    }

    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(LocalDate requiredDate) {
        this.requiredDate = requiredDate;
    }

    public LocalDate getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(LocalDate shippedDate) {
        this.shippedDate = shippedDate;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    /**
     * https://dzone.com/articles/java-string-format-examples
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("OrderId %d, orderDate %tF", getId(), getOrderDate());
    }

    public JsonObjectBuilder getJsonObjectBuilder() {
        final JsonObjectBuilder jsonObjectBuilder =
                Json.createObjectBuilder()
                        .add("id", this.id)
                        .add("customer", this.customer.getJsonObjectBuilder())
                        .add("orderDate", String.valueOf(this.orderDate))
                        .add("requiredDate", String.valueOf(this.requiredDate))
                        .add("shippedDate", String.valueOf(this.shippedDate))
                        .add("freight", this.freight);
        return jsonObjectBuilder;
    }


}
