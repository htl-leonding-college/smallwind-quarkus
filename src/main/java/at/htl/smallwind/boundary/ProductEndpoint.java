package at.htl.smallwind.boundary;

import at.htl.smallwind.control.InitBean;
import at.htl.smallwind.control.ProductDao;
import at.htl.smallwind.entity.Product;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductEndpoint {

    @Inject
    ProductDao productDao;

    @Inject
    InitBean initBean;

//    @GET
//    @Path("init")
//    public void init() {
//        initBean.startup();
//    }

    /**
     *
     * @return sämtliche Produkte im Json-format
     */
    @GET
    public Response findAll() {
        final List<Product> products =  productDao.findAll();
        return Response.ok(products).build();
    }

    //@GET


    /**
     * Erstellen eines Products in der DB
     * Es ist keine Fehlerbehandlung vorzusehen
     */
    @POST
    public Response create(Product product, @Context UriInfo info) {
        final Product savedProduct = productDao.save(product);
        URI uri = info.getAbsolutePathBuilder().path("/product/" + savedProduct.getId()).build();
        return Response.created(uri).build();
    }

    /**
     * ändern eines Products aus der DB
     * Es ist keine Fehlerbehandlung vorzusehen
     */
    @PUT
    public Response update(Product product) {
        return null;
    }

    /**
     * löschen eines Products aus der DB
     * Es ist keine Fehlerbehandlung vorzusehen
     */
    @DELETE
    @Path("product/{id}")
    public Response delete(@PathParam("id") long id) {
        productDao.delete(id);
        return Response.noContent().build();
    }


}
