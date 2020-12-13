package at.htl.smallwind.entity;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
@Table(name = "SW_PRODUCT")
@NamedQueries({
        @NamedQuery(
                name = "Product.findAll",
                query = "select p from Product p order by p.name"
        )
})
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROD_ID")
    private Long id;

    @Column(name = "PROD_NAME")
    String name;
    @Column(name = "PROD_UNIT_PRICE")
    private double unitPrice;
    @Column(name = "PROD_UNITS_IN_STOCK")
    private int unitsInStock;
    @Column(name = "PROD_UNITS_ON_ORDER")
    private int unitsOnOrder;
    @Column(name = "PROD_REORDER_LEVEL")
    private int reorderLevel;
    @Column(name = "PROD_DISCONTINUED")
    private boolean discontinued;

    public Product() { }

    public Product(String name, double unitPrice, int unitsInStock, int unitsOnOrder, int reorderLevel, boolean discontinued) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.unitsOnOrder = unitsOnOrder;
        this.reorderLevel = reorderLevel;
        this.discontinued = discontinued;
    }

    public Product(String line) {

        String[] elems = line.replaceAll("\"","").split(",");
        this.name = elems[1];
        this.unitPrice = Double.parseDouble(elems[2]);
        this.unitsInStock = Integer.parseInt(elems[3]);
        this.unitsOnOrder = Integer.parseInt(elems[4]);
        this.reorderLevel = Integer.parseInt(elems[5]);
        this.discontinued = elems[6].equals("1");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnitsOnOrder() {
        return unitsOnOrder;
    }

    public void setUnitsOnOrder(int unitsOnOrder) {
        this.unitsOnOrder = unitsOnOrder;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }

    public int getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(int unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    @Override
    public String toString() {
        return String.format("%d: %s",getId(),getName());
    }

    @JsonbTransient
    public JsonObjectBuilder getJsonObjectBuilder() {
        final JsonObjectBuilder jsonObjectBuilder =
                Json.createObjectBuilder()
                        .add("id", this.id)
                        .add("name", this.name)
                        .add("unitPrice", this.unitPrice)
                        .add("unitsInStock", this.unitsInStock)
                        .add("unitsInOrder", this.unitsOnOrder)
                        .add("reorderLevel", this.reorderLevel)
                        .add("discontinued", this.discontinued);
        return jsonObjectBuilder;
    }
}
