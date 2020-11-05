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

import ibm.gse.eda.inventory.domain.Store;
import io.quarkus.panache.common.Sort;

@Path("/stores")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class StoreResource {

    @GET
    public List<Store> get() {
        return Store.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public Store getSingle(@PathParam Long id) {
        Store entity = Store.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Store with id of " + id + " does not exist.", 404);
        }
        return entity;
    }


    @GET
    @Path("name/{name}")
    public Store getStoreByName(@PathParam String name) {
        Store entity = Store.find("name", name).firstResult();
        if (entity == null) {
            throw new WebApplicationException("Store with code of " + name + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Store Store) {
        if (Store.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        Store.persist();
        return Response.ok(Store).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Store update(@PathParam Long id, Store store) {
        if (store.name == null) {
            throw new WebApplicationException("Store Name was not set on request.", 422);
        }

        Store entity = Store.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Store with id of " + id + " does not exist.", 404);
        }

        entity.name = store.name;
        entity.state = store.state;
        entity.city = store.city;

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
        Store entity = Store.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Store with id of " + id + " does not exist.", 404);
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