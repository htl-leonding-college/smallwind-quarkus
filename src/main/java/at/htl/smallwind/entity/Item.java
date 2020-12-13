package at.htl.smallwind.entity;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;

/**
 * "ITM_ID","ITM_ORD_ID","ITM_PROD_ID","ITM_UNITPRICE","ITM_QUANTITY","ITM_DISCOUNT"
 * 563,10335,2,15.2,7,0.2
 */
@Entity
@Table(name = "SW_ITEM")
@NamedQueries({
        @NamedQuery(
                name="Item.findAll",
                query = "select i from Item i"
        ),
        @NamedQuery(
                name = "Item.findByOrderingId",
                query = "select i from Item i where i.ordering.id = :ORD_ID"
        )
})
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITM_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ITM_ORD_ID")
    private Ordering ordering;

    @ManyToOne
    @JoinColumn(name = "ITM_PROD_ID")
    private Product product;

    @Column(name = "ITM_UNIT_PRICE")
    private double unitPrice;
    @Column(name = "ITM_QUANTITY")
    private int quantity;
    @Column(name = "ITM_DISCOUNT")
    private double discount;

    public Item() { }

    public Item(Ordering ordering, Product product, double unitPrice, int quantity, double discount) {
        this.ordering = ordering;
        this.product = product;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public Ordering getOrdering() {
        return ordering;
    }

    public void setOrdering(Ordering ordering) {
        this.ordering = ordering;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", ordering=" + ordering +
                ", product=" + product +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", discount=" + discount +
                '}';
    }

    public JsonObjectBuilder getJsonObjectBuilder() {
        final JsonObjectBuilder jsonObjectBuilder =
                Json.createObjectBuilder()
                        .add("id", this.id)
                        .add("ordering", this.ordering.getJsonObjectBuilder())
                        .add("product", this.product.getJsonObjectBuilder())
                        .add("unitPrice", this.unitPrice)
                        .add("quantity", this.quantity)
                        .add("discount", this.discount);
        return jsonObjectBuilder;
    }
}
