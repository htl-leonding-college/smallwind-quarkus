package at.htl.smallwind.control;

import at.htl.smallwind.entity.Customer;
import at.htl.smallwind.entity.Ordering;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

@ApplicationScoped
public class OrderingDao {

    @Inject
    CustomerDao customerDao;

    private static final String FILE_NAME = "ordering.csv";

    @Inject
    EntityManager em;

    public Ordering findById(long id) {
        Ordering ordering = em.find(Ordering.class, id);
        return ordering;
    }


    /**
     *
     * @return all Orders
     */
    public List<Ordering> findAll() {
        return em
                .createNamedQuery("Ordering.findAll",Ordering.class)
                .getResultList();
    }

    public Map<String,Integer> countByCountries() {
        Query query = em.createNamedQuery("Ordering.countByCountry");
        List<Object[]> counts = query.getResultList();

        // Kontrollausgabe, ob Query funktioniert
        for (Object[] entry : counts) {
            out.println(entry[0] + " - " + entry[1]);
        }
        Map<String, Integer> ordersPerCountry = new HashMap<>();

        counts
                .stream()
                .peek(out::println)
                .forEach(c -> ordersPerCountry.put(
                        c[0].toString(),
                        Integer.valueOf(c[1].toString())
                ));

        for (Map.Entry<String, Integer> entry : ordersPerCountry.entrySet()) {
            out.println(entry.getKey() + " -- " + entry.getValue());
        }
        return ordersPerCountry;
    }


    /**
     * Importing the csv-file using a lambda-stream.
     *
     * The stream elements must be sorted, so the continouing id's are correct.
     */
    @Transactional
    public void readFromCsv() {
//        new BufferedReader(new InputStreamReader(this.getClass()
//                .getResourceAsStream(FILE_NAME), StandardCharsets.UTF_8))
        InputStream is = getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        br
                .lines()
                .skip(1)
                .sorted()
                .map(this::createOrdering)
                .peek(out::println)
                .forEach(em::merge);
    }

    /**
     * one line of the csv-file is parsed.
     * When a two-digit-year of a date is < 50 the base year is 2000 i.e. 07 -> 2007
     * When a two-digit-year of a date is >= 50 the base year is 1900 i.e. 87 -> 1987
     *
     * A customer is to retreive from CustomerDao by id
     *
     * "ORD_ID","ORD_CUST_ID","ORD_ORDERDATE","ORD_REQUIREDDATE","ORD_SHIPPEDDATE","ORD_FREIGHT"
     */
    Ordering createOrdering(String line) {

        DateTimeFormatter dtf = null;

        // starts with base year 2000, so we get instead of 1997 -> 2097
        DateTimeFormatter dtf2000 = DateTimeFormatter.ofPattern("dd.MM.yy");

        // starts with base year 1900
        DateTimeFormatter dtf1900 = new DateTimeFormatterBuilder()
                .appendPattern("dd.MM.")
                .appendValueReduced(ChronoField.YEAR, 2, 2, 1900)
                .toFormatter();

        String[] elems = line.replace("\n", "").split(",");

        Customer customer = customerDao.findById(Long.parseLong(elems[1]));

        dtf = (Integer.parseInt(elems[2].substring(6)) >= 50 ? dtf1900 : dtf2000);
        LocalDate orderDate = LocalDate.parse(elems[2], dtf);

        dtf = (Integer.parseInt(elems[2].substring(6)) >= 50 ? dtf1900 : dtf2000);
        LocalDate requiredDate = LocalDate.parse(elems[3], dtf);

        dtf = (Integer.parseInt(elems[2].substring(6)) >= 50 ? dtf1900 : dtf2000);
        LocalDate shippedDate = (elems[4].isBlank() ? null : LocalDate.parse(elems[4], dtf));

        double freight = Double.parseDouble(elems[5]);

        return new Ordering(customer, orderDate, requiredDate, shippedDate, freight);
    }

}
