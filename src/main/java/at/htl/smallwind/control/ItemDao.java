package at.htl.smallwind.control;

import at.htl.smallwind.entity.Item;
import at.htl.smallwind.entity.Ordering;
import at.htl.smallwind.entity.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * "ITM_ID","ITM_ORD_ID","ITM_PROD_ID","ITM_UNITPRICE","ITM_QUANTITY","ITM_DISCOUNT"
 * 563,10335,2,15.2,7,0.2
 */
@ApplicationScoped
public class ItemDao {

    @Inject
    OrderingDao orderingDao;

    @Inject
    ProductDao productDao;

    private static final String FILE_NAME = "item.csv";

    @Inject
    EntityManager em;

    /**
     *
     * @param id of item
     * @return item
     */
    public Item findById(long id) {
        return em.find(Item.class, id);
    }

    /**
     *
     * @return all items
     */
    public List<Item> findAll() {
        return em
                .createNamedQuery("Item.findAll",Item.class)
                .getResultList();
    }

    /**
     *
     * @param orderId
     * @return all items belonging to the order with the given orderId
     */
    public List<Item> findByOrderId(long orderId) {
        return em
                .createNamedQuery("Item.findByOrderingId",Item.class)
                .setParameter("ORD_ID", orderId)
                .getResultList();
    }


    /**
     * read from given csv-file.
     * Use a lambda stream to create item-object and persist these objects in the database.
     * Use the method "createItem(...)" for creating the item-objects
     */
    @Transactional
    public void readFromCsv() {
//        new BufferedReader(new InputStreamReader(this.getClass()
//                .getResourceAsStream(FILE_NAME), StandardCharsets.UTF_8))
        InputStream is = getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        br.lines()
                .skip(1)
                .map(this::createItem)
                .peek(System.out::println)
                .forEach(em::merge);
    }

    /**
     * Parse the elements of the csv-line and populate a new item-object
     *
     * @param line of the csv-file
     * @return return the created item-object
     *
     * Comment: this method is package scoped for testing purposes
     */
    @Transactional
    Item createItem(String line) {
        String[] elems = line.split(",");
        Ordering ordering = orderingDao.findById(Long.parseLong(elems[1]));
        Product product = productDao.findById(Long.parseLong(elems[2]));
        double unitPrice = Double.parseDouble(elems[3]);
        int quantity = Integer.parseInt(elems[4]);
        double discount = Double.parseDouble(elems[5]);
        return new Item(ordering, product, unitPrice, quantity, discount);
    }

}
