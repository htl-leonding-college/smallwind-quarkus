package at.htl.smallwind.boundary;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://phauer.com/2016/testing-restful-services-java-best-practices/
 *
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class OrderingEndpointTest {

    @BeforeAll
    private static void init() {
        //RestAssured.basePath = "/smallwind/api";
    }


    /**
     * prefer using rest-assured and assert-j
     *
     * Converting the JSON to a JsonPath Object and use AssertJ to check it
     * source: https://phauer.com/2016/testing-restful-services-java-best-practices/
     *
     * This JsonPath - Library is used:
     * https://www.javadoc.io/doc/io.rest-assured/json-path/3.0.0/io/restassured/path/json/JsonPath.html
     */
    @Test
    void t120_listAllOrderings() {
        JsonPath retrievedOrders = given()
        .when()
            .get("orders")
        .then()
            .assertThat()
            .statusCode(200)
            .contentType("application/json")
            .log().body()
            .extract().jsonPath();

        assertThat(retrievedOrders.getList("").size()).isEqualTo(830);
        assertThat(retrievedOrders.getInt("[0].customer.id")).isEqualTo(65);
        assertThat(retrievedOrders.getString("[0].orderDate")).isEqualTo("1998-05-06");
        assertThat(retrievedOrders.getInt("[4].customer.id")).isEqualTo(58);
        assertThat(retrievedOrders.getString("[4].orderDate")).isEqualTo("1998-05-05");

    }

    @Test
    void t130_findOneOrderingByIdAndListWithItems() {
        JsonPath retrievedOrder = given()
            .when()
                .queryParam("orderid", "10277")
                .get("orders/order")
            .then()
                .assertThat()
                .statusCode(200)
                .contentType("application/json")
                .log().body()
                .extract().jsonPath();

        assertThat(retrievedOrder.getInt("id")).isEqualTo(10277);
        assertThat(retrievedOrder.getString("orderDate")).isEqualTo("1996-08-09");
        assertThat(retrievedOrder.getInt("customer.id")).isEqualTo(52);

        assertThat(retrievedOrder.getInt("items[0].item.id")).isEqualTo(1242);
        assertThat(retrievedOrder.getInt("items[0].item.ordering.id")).isEqualTo(10277);
        assertThat(retrievedOrder.getInt("items[0].item.ordering.customer.id")).isEqualTo(52);
        assertThat(retrievedOrder.getInt("items[0].item.product.id")).isEqualTo(28);
        assertThat(retrievedOrder.getString("items[0].item.product.name")).isEqualTo("Rössle Sauerkraut");

        assertThat(retrievedOrder.getInt("items[1].item.id")).isEqualTo(1243);
        assertThat(retrievedOrder.getInt("items[1].item.ordering.id")).isEqualTo(10277);
        assertThat(retrievedOrder.getInt("items[1].item.ordering.customer.id")).isEqualTo(52);
        assertThat(retrievedOrder.getInt("items[1].item.product.id")).isEqualTo(62);
        assertThat(retrievedOrder.getString("items[1].item.product.name")).isEqualTo("Tarte au sucre");

    }






















    /**
     * This test uses rest-assured and hamcrest -> don't want to use it
     */
//    @Test
//    @Disabled
//    void t100_listAllOrderings() {
//        given()
//        .when()
//            .get("orders")
//        .then()
//            .assertThat()
//            .statusCode(200)
//            .contentType("application/json")
//            .body("customer.abbr",hasItem("VINET"))
//            .body("customer.ccardNo",empty())
//            .body("customer.country",equalTo("France"))
//            .log()
//            .body();
//    }


}
