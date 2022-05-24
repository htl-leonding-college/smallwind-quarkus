package at.htl.smallwind.control;

import at.htl.smallwind.entity.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.System.out;

@ApplicationScoped
public class CustomerDao {

    private static final String FILE_NAME = "customer.csv";

    @Inject
    EntityManager em;

    @Transactional
    public void readFromCsv() {
        //new BufferedReader(new InputStreamReader(this.getClass()
        //       .getResourceAsStream(FILE_NAME), StandardCharsets.UTF_8))

        // https://github.com/quarkusio/quarkus/issues/2746#issuecomment-506941113
        // https://github.com/quarkusio/quarkus/pull/2910#issuecomment-504671096
        /*
        InputStream is = getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        br.lines()
                .skip(1)
                .map(s -> s.split(","))
                .map(a -> new Customer(
                        a[1].replace("\"", ""),
                        a[2].replace("\"", ""),
                        a[3].replace("\"", ""),
                        a[4].replace("\"", "")))
                .peek(out::println)
                .forEach(em::merge);
        //.forEach(out::println);
         */
        URL url = Thread.currentThread().getContextClassLoader().getResource(FILE_NAME);
        try (Stream<String> stream = Files.lines(Paths.get(url.getPath()), StandardCharsets.UTF_8)) {
            stream.skip(1)
                    .map(s -> s.split(","))
                    .map(a -> new Customer(
                            a[1].replace("\"", ""),
                            a[2].replace("\"", ""),
                            a[3].replace("\"", ""),
                            a[4].replace("\"", "")))
                    .peek(out::println)
                    .forEach(em::merge);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Object[]> countPerCountry() {
        Query query = em.createQuery("select c.country, count(c) from Customer c group by c.country");
        List<Object[]> counts = query.getResultList();
        return counts;
    }

    public Customer findById(long id) {
        return em.find(Customer.class, id);
    }


//    public void readFromCsv() {
//        ClassLoader classLoader = getClass().getClassLoader();
//        File file = new File(classLoader.getResource(FILE_NAME).getFile());
//        String line = null;
//        boolean firstline = true;  // csv-File contains a header line
//
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(file));
//
//            if (firstline) {
//                br.readLine();
//            }
//
//            while ((line = br.readLine()) != null) {
//                String[] el = line.split(",");
//                em.persist(new Customer(
//                        el[1].replaceAll("\"",""),
//                        el[2].replaceAll("\"",""),
//                        el[3].replaceAll("\"",""),
//                        el[4].replaceAll("\"","")
//                ));
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
