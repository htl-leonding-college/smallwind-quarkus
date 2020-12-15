package at.htl.smallwind.control;

import at.htl.smallwind.entity.Customer;
import at.htl.smallwind.entity.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.System.out;

@ApplicationScoped
public class ProductDao {

    private static final String FILE_NAME = "product.csv";

    @Inject
    EntityManager em;


    public Product findById(long id) {
        return em.find(Product.class, id);
    }

    @Transactional
    public Product save(Product product) {
        return em.merge(product);
    }

    public List<Product> findAll() {
        return em
                .createNamedQuery("Product.findAll", Product.class)
                .getResultList();
    }

    @Transactional
    public void delete(long id) {
        Product productToDelete = em.find(Product.class, id);
        em.remove(productToDelete);
    }

    @Transactional
    public void readFromCsv() {
////        new BufferedReader(new InputStreamReader(this.getClass()
////                .getResourceAsStream(FILE_NAME), StandardCharsets.UTF_8))
//        InputStream is = getClass().getClassLoader().getResourceAsStream(FILE_NAME);
//        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
//        br.lines()
//                .skip(1)
//                .map(line -> new Product(line))
//                .peek(out::println)
//                .forEach(em::merge);
//                //.forEach(out::println);
        URL url = Thread.currentThread().getContextClassLoader().getResource(FILE_NAME);
        try {
            assert url != null;
            try (Stream<String> stream = Files.lines(Paths.get(url.getPath()), StandardCharsets.UTF_8)) {
                stream.skip(1)
                        //.map(line -> new Product(line))
                        .map(Product::new)
                        .peek(out::println)
                        .forEach(em::merge);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
