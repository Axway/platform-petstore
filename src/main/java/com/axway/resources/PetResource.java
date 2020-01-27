package com.axway.resources;

import com.axway.api.Pet;
import com.axway.db.PetDAO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Resource class based around {@link Pet} models.
 */
@Path("/api/v1/pet")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PetResource {

    /**
     * The data class backing this resource, used to store pets.
     */
    private final PetDAO pets;

    /**
     * Constructs a new resource with a storage instance.
     *
     * @param pets
     *      the storage instance used to store/fetch {@link Pet} instances.
     */
    public PetResource(PetDAO pets) {
        this.pets = pets;
    }

    /**
     * Retrieves all {@link Pet} instances.
     *
     * @param limit
     *      a parameter used to limit the amount of retrieved pets.
     * @return
     *      a collection of {@link Pet} instances.
     */
    @GET
    public Collection<Pet> find(@QueryParam("limit") Optional<Integer> limit) {
        return this.pets.get()
                .sorted(Comparator.comparing(Pet::getId))
                .limit(limit.filter(i -> i <= 100).orElse(100))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a {@link Pet} by an identifier.
     *
     * @param petId
     *      the identifier of the pet to retrieve.
     * @return
     *      a potential {@link Pet} instance if found.
     */
    @GET
    @Path("/{petId}")
    public Optional<Pet> findById(@PathParam("petId") String petId) {
        return this.pets.get(petId);
    }

    /**
     * Creates a new {@link Pet} instance.
     *
     * @param pet
     *      the {@link Pet} instance to store.
     */
    @POST
    public void create(Pet pet) {
        this.pets.create(pet);
    }
}
