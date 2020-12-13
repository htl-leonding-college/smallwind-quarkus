package at.htl.smallwind.boundary;

import at.htl.smallwind.control.ItemDao;
import at.htl.smallwind.control.OrderingDao;
import at.htl.smallwind.entity.Item;
import at.htl.smallwind.entity.Ordering;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderingEndpoint {

    @Inject
    OrderingDao orderingDao;

    @Inject
    ItemDao itemDao;

    /**
     * @return a list of all Orderings w/o items (with customers)
     */
    @GET
    public Response listAllOrders() {
        List<Ordering> orders = orderingDao.findAll();
        return Response.ok(orders).build();
    }

    /**
     *
     * @param orderId
     * @return
     */
    @GET
    @Path("order")
    public JsonObject findByOrderingId(@QueryParam("orderid") long orderId) {

        Ordering ordering = orderingDao.findById(orderId);
        List<Item> items = itemDao.findByOrderId(orderId);

        // get the Json-Object from Ordering
        JsonObjectBuilder jsonObjectBuilderOrdering = ordering.getJsonObjectBuilder();

        final JsonArrayBuilder arrayBuilderItems = Json.createArrayBuilder();
        // iterate all items and add each item to the ordering
        for (Item item : items) {
            arrayBuilderItems.add(
                    Json.createObjectBuilder()
                    .add("item",item.getJsonObjectBuilder())
            );
        }

        // add a JsonArray to the ordering-JsonObject named "items"
        jsonObjectBuilderOrdering.add("items", arrayBuilderItems);

        return jsonObjectBuilderOrdering.build();
    }
}
