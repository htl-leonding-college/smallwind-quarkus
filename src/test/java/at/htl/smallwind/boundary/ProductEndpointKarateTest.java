package at.htl.smallwind.boundary;

import com.intuit.karate.junit5.Karate;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ProductEndpointKarateTest {

    @Karate.Test
    Karate t200_createProduct() {
        return Karate.run("product-creation").relativeTo(getClass());
    }

}
