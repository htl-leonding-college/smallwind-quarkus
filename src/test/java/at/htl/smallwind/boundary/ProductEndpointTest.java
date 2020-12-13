package at.htl.smallwind.boundary;

import at.htl.smallwind.control.DatabaseHelper;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.assertj.db.type.Request;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.sql.DataSource;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.db.output.Outputs.output;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class ProductEndpointTest {

    private static DataSource ds;

    @BeforeAll
    private static void init() {
        ds = DatabaseHelper.getDatasource();
    }

    @Test
    void t100_listaAllProducts() {
        JsonPath retrievedProducts = given()
            .when()
                .get("products")
            .then()
                .assertThat()
                .statusCode(200)
                .contentType("application/json")
                .log().body()
                .extract().jsonPath();

        assertThat(retrievedProducts.getList("").size()).isEqualTo(77);

        assertThat(retrievedProducts.getBoolean("[0].discontinued")).isEqualTo(true);
        assertThat(retrievedProducts.getInt("[0].id")).isEqualTo(17);
        assertThat(retrievedProducts.getString("[0].name")).isEqualTo("Alice Mutton");
        assertThat(retrievedProducts.getInt("[0].reorderLevel")).isEqualTo(0);
        assertThat(retrievedProducts.getDouble("[0].unitPrice")).isEqualTo(39.0);
        assertThat(retrievedProducts.getInt("[0].unitsOnOrder")).isEqualTo(0);

        assertThat(retrievedProducts.getBoolean("[75].discontinued")).isEqualTo(false);
        assertThat(retrievedProducts.getInt("[75].id")).isEqualTo(64);
        assertThat(retrievedProducts.getString("[75].name")).isEqualTo("Wimmers gute Semmelknödel");
        assertThat(retrievedProducts.getInt("[75].reorderLevel")).isEqualTo(30);
        assertThat(retrievedProducts.getDouble("[75].unitPrice")).isEqualTo(33.25);
        assertThat(retrievedProducts.getInt("[75].unitsOnOrder")).isEqualTo(80);

        assertThat(retrievedProducts.getBoolean("[76].discontinued")).isEqualTo(false);
        assertThat(retrievedProducts.getInt("[76].id")).isEqualTo(47);
        assertThat(retrievedProducts.getString("[76].name")).isEqualTo("Zaanse koeken");
        assertThat(retrievedProducts.getInt("[76].reorderLevel")).isEqualTo(0);
        assertThat(retrievedProducts.getDouble("[76].unitPrice")).isEqualTo(9.5);
        assertThat(retrievedProducts.getInt("[76].unitsOnOrder")).isEqualTo(0);

    }

    @Test
    void t110_postNewProduct() {

        // given
        final JsonObject product = Json.createObjectBuilder()
                .add("name", "Topfenstrudel")
                .add("unitPrice", 15.0)
                .add("unitsInStock", 10)
                .add("unitsOnOrder", 5)
                .add("reorderLevel", 7)
                .add("discontinued", false)
                .build();
        System.out.println(product.toString());

        // when
        String locationHeader = given()
                .header("Content-Type","application/json")
                .body(product.toString())
            .when()
                .post("/products")
            .then()
                .statusCode(anyOf(is(200),is(201)))
                .log().headers()  // Ausgabe am Bildschirm
                .extract().header("Location");




        // then
        //Table productTable = new Table(ds, DatabaseHelper.PRODUCT_TABLE);
        final String sql = "select prod_id, prod_discontinued, prod_name, prod_reorder_level, prod_unit_price, prod_units_in_stock, prod_units_on_order "
                + "from " + DatabaseHelper.PRODUCT_TABLE + " where prod_name = 'Topfenstrudel'";
        Request topfenstrudel = new Request(ds, sql);
        output(topfenstrudel).toConsole();

        // get id from database
        int storedId = Long.valueOf(topfenstrudel
            .getRow(0)
            .getColumnValue("prod_id")
            .getValue()
            .toString()
        ).intValue();
        System.out.println("id from database: " + storedId);
        assertThat(locationHeader).isEqualTo("http://localhost:8080/products/product/" + storedId);

        org.assertj.db.api.Assertions.assertThat(topfenstrudel).row()
                .value().isGreaterThan(77)
                .value().isEqualTo(0)
                .value().isEqualTo("Topfenstrudel")
                .value().isEqualTo(7)
                .value().isEqualTo(15.0)
                .value().isEqualTo(10)
                .value().isEqualTo(5);
    }

    @Test
    void t120_deleteProductTopfenstrudel() {
        // given
        final String sql = "select prod_id, prod_discontinued, prod_name, prod_reorder_level, prod_unit_price, prod_units_in_stock, prod_units_on_order "
                + "from " + DatabaseHelper.PRODUCT_TABLE + " where prod_name = 'Topfenstrudel'";
        Request topfenstrudel = new Request(ds, sql);

        // überprüfe, ob die zu löschende Zeile vorhanden ist
        assertThat(topfenstrudel.getRowsList().size()).isEqualTo(1);
        output(topfenstrudel).toConsole();

        // What is the actual id of the Topfenstrudel?
        int storedId = Long.valueOf(topfenstrudel
                .getRow(0)
                .getColumnValue("prod_id")
                .getValue()
                .toString()
        ).intValue();
        System.out.println("id: = " + storedId);

        // when
        given()
            .when()
                .delete("products/product/" + storedId)
            .then()
                .statusCode(anyOf(is(200),is(204))); // sollte eigentlich 204 NO CONTENT sein

        // then
        topfenstrudel = new Request(ds, sql);
        output(topfenstrudel).toConsole();
        assertThat(topfenstrudel.getRowsList().size()).isEqualTo(0);
    }



}
