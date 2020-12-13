package at.htl.smallwind.control;

import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class InitBean {

    @Inject
    private Logger logger;

    @Inject
    CustomerDao customerDao;

    @Inject
    ProductDao productDao;

    @Inject
    OrderingDao orderingDao;

    @Inject
    ItemDao itemDao;

    public InitBean() {
    }

    void onStart(@Observes StartupEvent ev) {
    //public void init() {
        customerDao.readFromCsv();
        productDao.readFromCsv();
        orderingDao.readFromCsv();
        itemDao.readFromCsv();

        List<Object[]> counts = customerDao.countPerCountry();
        for (Object[] c : counts) {
            logger.info(String.format("%-20s: %3d",c[0], c[1]));
        }

    }
}
