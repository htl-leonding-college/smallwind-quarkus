package at.htl.smallwind.control;

import at.htl.smallwind.entity.Customer;
import at.htl.smallwind.entity.Ordering;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.json.JsonObject;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.MethodName.class)
class OrderingDaoTest {

    @Test
    void t100_createOrderingBase1900() {

        // arrange, given
        OrderingDao orderingDao = new OrderingDao();
        CustomerDao mockedCustomerDao = mock(CustomerDao.class);
        String line = "10595,20,10.07.97,07.08.97,14.07.97,96.78";
        orderingDao.customerDao = mockedCustomerDao;
        when(mockedCustomerDao.findById(20L)).thenReturn(new Customer("a", "b", "c", "d"));

        // act, when
        Ordering ordering = orderingDao.createOrdering(line);

        // assert, then
        assertThat(ordering.getOrderDate().getYear()).isEqualTo(1997);
        assertThat(ordering.getOrderDate().getMonth()).isEqualTo(Month.JULY);
        assertThat(ordering.getOrderDate().getDayOfMonth()).isEqualTo(10);

        assertThat(ordering.getRequiredDate().getYear()).isEqualTo(1997);
        assertThat(ordering.getRequiredDate().getMonth()).isEqualTo(Month.AUGUST);
        assertThat(ordering.getRequiredDate().getDayOfMonth()).isEqualTo(7);

        assertThat(ordering.getShippedDate().getYear()).isEqualTo(1997);
        assertThat(ordering.getShippedDate().getMonth()).isEqualTo(Month.JULY);
        assertThat(ordering.getShippedDate().getDayOfMonth()).isEqualTo(14);
    }

    @Test
    void t110_createOrderingBase2000() {

        // arrange
        OrderingDao orderingDao = new OrderingDao();
        CustomerDao mockedCustomerDao = mock(CustomerDao.class);
        String line = "10595,20,10.07.07,07.08.07,14.07.07,96.78";
        orderingDao.customerDao = mockedCustomerDao;
        when(mockedCustomerDao.findById(20L)).thenReturn(new Customer("a", "b", "c", "d"));


        // act
        Ordering ordering = orderingDao.createOrdering(line);

        // assert
        assertThat(ordering.getOrderDate().getYear()).isEqualTo(2007);
        assertThat(ordering.getOrderDate().getMonth()).isEqualTo(Month.JULY);
        assertThat(ordering.getOrderDate().getDayOfMonth()).isEqualTo(10);

        assertThat(ordering.getRequiredDate().getYear()).isEqualTo(2007);
        assertThat(ordering.getRequiredDate().getMonth()).isEqualTo(Month.AUGUST);
        assertThat(ordering.getRequiredDate().getDayOfMonth()).isEqualTo(7);

        assertThat(ordering.getShippedDate().getYear()).isEqualTo(2007);
        assertThat(ordering.getShippedDate().getMonth()).isEqualTo(Month.JULY);
        assertThat(ordering.getShippedDate().getDayOfMonth()).isEqualTo(14);
    }

    /**
     *
     * Testing the getJsonObjectBuilder()-method in Ordering
     *
     * https://o7planning.org/de/10155/anleitung-java-reflection
     */
    @Test
    void t120_getJsonObjectBuilderFromOrdering() {

        Customer customer = new Customer("a", "b", "c", "");
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

        System.out.println(ordering.getJsonObjectBuilder().build().toString());

        JsonObject o = ordering.getJsonObjectBuilder().build();

        assertThat(o.getInt("id")).isEqualTo(100);
        assertThat(o.getJsonObject("customer").getInt("id")).isEqualTo(20);
        assertThat(o.getJsonObject("customer").getString("name")).isEqualTo("a");
        assertThat(o.getJsonObject("customer").getString("abbr")).isEqualTo("c");
        assertThat(o.getJsonObject("customer").getString("ccardno")).isEmpty();
        assertThat(o.getString("orderDate")).isEqualTo("2020-01-16");
        assertThat(o.getString("requiredDate")).isEqualTo("2020-01-17");
        assertThat(o.getString("shippedDate")).isEqualTo("2020-01-18");
        assertThat(o.getJsonNumber("freight").doubleValue()).isEqualTo(100.0);
    }

}