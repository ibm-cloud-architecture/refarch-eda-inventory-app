package ibm.gse.eda.inventory.api;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import ibm.gse.eda.inventory.domain.Item;
import io.quarkus.panache.common.Sort;

@Path("/items")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class ItemResource {

    @GET
    public List<Item> get() {
        return Item.listAll(Sort.by("title"));
    }

    @GET
    @Path("{id}")
    public Item getSingle(@PathParam Long id) {
        Item entity = Item.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Item with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @GET
    @Path("name/{code}")
    public Item getItemByCode(@PathParam String code) {
        Item entity = Item.find("code", code).firstResult();
        if (entity == null) {
            throw new WebApplicationException("Item with code of " + code + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Item Item) {
        if (Item.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        Item.persist();
        return Response.ok(Item).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Item update(@PathParam Long id, Item item) {
        if (item.title == null) {
            throw new WebApplicationException("Item Name was not set on request.", 422);
        }

        Item entity = Item.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Item with id of " + id + " does not exist.", 404);
        }

        entity.title = item.title;
        entity.price = item.price;
        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
        Item entity = Item.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Item with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Override
        public Response toResponse(Exception exception) {
            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }
            return Response.status(code)
                    .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build())
                    .build();
        }

    }
}