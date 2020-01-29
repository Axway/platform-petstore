package com.axway.db;

import com.axway.api.Event;
import com.axway.api.Pet;
import com.axway.client.mbaas.MbaasClient;
import com.axway.client.pubsub.PubSubClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import io.dropwizard.jackson.Jackson;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.axway.api.Event.PET_CREATE_EVENT;
import static com.axway.api.Event.PET_REMOVE_EVENT;

/**
 * MBaaS based storage class for Pet instances.
 *
 * This will store Pet instances in a remote MBaaS application datastore,
 * so that it can be used from multiple instances of the application.
 */
public class PetMbaasDAO implements PetDAO {

    /**
     * Singleton JSON mapper to use when translating JSON between containers.
     */
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    /**
     * The internal client used to talk to MBaaS.
     */
    private final MbaasClient client;

    /**
     * The PubSub client used to broadcast events on creation.
     */
    private final PubSubClient pubsub;

    /**
     * Initializes a new DAO with an MBaaS client.
     *
     * @param client
     *      the client used for remote connections.
     * @param pubsub
     *      the client used for PubSub event emission.
     */
    public PetMbaasDAO(@Nonnull MbaasClient client, @Nonnull PubSubClient pubsub) {
        this.client = Objects.requireNonNull(client);
        this.pubsub = Objects.requireNonNull(pubsub);
    }

    /**
     * Create a new {@link Pet} instance.
     *
     * @param pet
     *      the {@link Pet} instance to store.
     */
    @Override
    public void create(Pet pet) throws IOException {
        RequestBody requestBody = new FormEncodingBuilder()
                .add("classname", "pets")
                .add("fields", MAPPER.writeValueAsString(pet))
                .build();

        Response response = this.client.call("POST", "/objects/pets/create.json", requestBody);

        if (response.code() != 200) {
            throw new IOException("Unable to create pet in MBaaS");
        }

        try (ResponseBody body = response.body()) {
            try (InputStream is = body.byteStream()) {
                pet = new Pet(
                    MAPPER.readTree(is).path("response").path("pets").path(0).path("id").asText(),
                    pet.getName(),
                    pet.getPhoto(),
                    pet.getType()
                );
            }
        }

        // The following block will emit a PubSub creation event, containing the Pet
        // object itself in the data payload. This allows people to respond to any
        // new pets being created.
        //
        // ObjectNode data = MAPPER.convertValue(pet, ObjectNode.class);
        // Event event = new Event(PET_CREATE_EVENT, data);
        // this.pubsub.send(event);
    }

    /**
     * Retrieve all {@link Pet} instances.
     *
     * @return a {@link Stream} of {@link Pet} instances.
     */
    @Override
    public Stream<Pet> get() throws IOException {
        Response response = this.client.call("GET", "/objects/pets/query.json");

        if (response.code() != 200) {
            throw new IOException("Unable to fetch pets from MBaaS");
        }

        try (ResponseBody body = response.body()) {
            try (InputStream is = body.byteStream()) {
                List<Pet> pets = new ArrayList<>();

                MAPPER.readTree(is).path("response").path("pets").forEach(node -> {
                    pets.add(MAPPER.convertValue(node, Pet.class));
                });

                return pets.stream();
            }
        }
    }

    /**
     * Retrieve a specific {@link Pet} instance.
     *
     * @param id
     *      the identifier of the {@link Pet} to retrieve.
     * @return
     *      a potential {@link Pet} instance if found.
     */
    @Override
    public Optional<Pet> get(String id) throws IOException {
        Response response = this.client.call("GET", "/objects/pets/show.json?id=" + id);

        if (response.code() != 200) {
            throw new IOException("Unable to fetch pet from MBaaS");
        }

        try (ResponseBody body = response.body()) {
            try (InputStream is = body.byteStream()) {
                JsonNode pets = MAPPER.readTree(is).path("response").path("pets");

                if (pets.path(0).isMissingNode()) {
                    return Optional.empty();
                }

                return Optional.of(MAPPER.convertValue(pets.path(0), Pet.class));
            }
        }
    }

    /**
     * Removes a {@link Pet} instance.
     *
     * @param id
     *      the identifier of the {@link Pet} to retrieve.
     */
    @Override
    public void remove(String id) throws IOException {
        Response response = this.client.call("GET", "/objects/pets/delete.json?id=" + id);

        if (response.code() != 200) {
            throw new IOException("Unable to remove pet from MBaaS");
        }

        // The following block will emit a PubSub removal event, containing the Pet
        // identifier. This allows people to respond to any old pets being removed.
        //
        // ObjectNode data = MAPPER.createObjectNode();
        // data.put("id", id);
        //
        // Event event = new Event(PET_REMOVE_EVENT, data);
        // this.pubsub.send(event);
    }
}
