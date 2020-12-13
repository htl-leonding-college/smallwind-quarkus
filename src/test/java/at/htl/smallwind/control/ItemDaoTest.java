package at.htl.smallwind.control;

import at.htl.smallwind.entity.Customer;
import at.htl.smallwind.entity.Item;
import at.htl.smallwind.entity.Ordering;
import at.htl.smallwind.entity.Product;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.json.JsonObject;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class ItemDaoTest {

    private static DataSource dataSource;

    @BeforeAll
    private static void init() {
        dataSource = DatabaseHelper.getDatasource();
    }

    @Test
    void t0100_itemsNoOfRows() {
        Table itemTable = new Table(dataSource, DatabaseHelper.ITEM_TABLE);
        //output(itemTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(itemTable).hasNumberOfRows(2155);
    }

    @Test
    void t0110_itemsNoOfColumns() {
        Table itemTable = new Table(dataSource, DatabaseHelper.ITEM_TABLE);
        org.assertj.db.api.Assertions.assertThat(itemTable).row().hasNumberOfColumns(6);
    }

    /**
     * The order of the columns is important
     */
    @Test
    void t0120_itemColumnNames() {
        Table itemTable = new Table(dataSource, DatabaseHelper.ITEM_TABLE);
        Assertions.assertThat(itemTable)
                .column().hasColumnName("ITM_ID")
                .column().hasColumnName("ITM_DISCOUNT")
                .column().hasColumnName("ITM_QUANTITY")
                .column().hasColumnName("ITM_UNIT_PRICE")
                .column().hasColumnName("ITM_ORD_ID")
                .column().hasColumnName("ITM_PROD_ID")
        ;
    }

    @Test
    void t0130_itemFirstRowData() {
        Table itemTable = new Table(dataSource, DatabaseHelper.ITEM_TABLE);
        Assertions.assertThat(itemTable).row(0)
                .value().isEqualTo(1L)
                .value().isEqualTo(0.2)
                .value().isEqualTo(7)
                .value().isEqualTo(15.2)
                .value().isEqualTo(10335)
                .value().isEqualTo(2)
        ;

        Assertions.assertThat(itemTable).row(493)
                .value().isEqualTo(494L)
                .value().isEqualTo(0)
                .value().isEqualTo(24)
                .value().isEqualTo(40)
                .value().isEqualTo(10522)
                .value().isEqualTo(8)
        ;
    }


    /**
     *
     * Testing the getJsonObjectBuilder()-method in Item
     *
     * https://o7planning.org/de/10155/anleitung-java-reflection
     */
    @Test
    void t130_getJsonObjectBuilderFromItem() {

        Customer customer = new Customer("a", "b", "c", "");
        Product product = new Product("a", 100.0, 12, 13, 14, false);
        Ordering ordering = new Ordering(
                customer,
                LocalDate.parse("2020-01-16"),
                LocalDate.parse("2020-01-17"),
                LocalDate.parse("2020-01-18"),
                100.0
        );

        // set private id in Ordering
        Class<Ordering> orderingClass = Ordering.class;
        try {
            Field idField = orderingClass.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(ordering, 100L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }

        // set private id in Customer
        Class<Customer> customerClass = Customer.class;
        try {
            Field idField = customerClass.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(customer, 20L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }

        // set private id in Product
        Class<Product> productClass = Product.class;
        try {
            Field idField = productClass.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(product, 2000L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }

        Item item = new Item(ordering, product, 150.0, 20, 0.1);

        // set private id in Item
        Class<Item> itemClass = Item.class;
        try {
            Field idField = itemClass.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(item, 600L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }




        System.out.println(item.getJsonObjectBuilder().build().toString());

        JsonObject i = item.getJsonObjectBuilder().build();

        assertThat(i.getInt("id")).isEqualTo(600);
        assertThat(i.getJsonObject("ordering").getJsonObject("customer").getInt("id")).isEqualTo(20);
        assertThat(i.getJsonObject("ordering").getJsonObject("customer").getString("name")).isEqualTo("a");
        assertThat(i.getJsonObject("ordering").getJsonObject("customer").getString("abbr")).isEqualTo("c");
        assertThat(i.getJsonObject("ordering").getJsonObject("customer").getString("ccardno")).isEmpty();
        assertThat(i.getJsonObject("ordering").getString("orderDate")).isEqualTo("2020-01-16");
        assertThat(i.getJsonObject("ordering").getString("requiredDate")).isEqualTo("2020-01-17");
        assertThat(i.getJsonObject("ordering").getString("shippedDate")).isEqualTo("2020-01-18");
        assertThat(i.getJsonObject("ordering").getJsonNumber("freight").doubleValue()).isEqualTo(100.0);
        assertThat(i.getJsonObject("product").getInt("id")).isEqualTo(2000);
        assertThat(i.getJsonObject("product").getInt("unitsInOrder")).isEqualTo(13);
        assertThat(i.getJsonObject("product").getInt("reorderLevel")).isEqualTo(14);
        assertThat(i.getJsonObject("product").getInt("unitsInStock")).isEqualTo(12);
        assertThat(i.getJsonNumber("unitPrice").doubleValue()).isEqualTo(150.0);
        assertThat(i.getJsonNumber("quantity").doubleValue()).isEqualTo(20.0);
        assertThat(i.getJsonNumber("discount").doubleValue()).isEqualTo(0.1);
    }


}