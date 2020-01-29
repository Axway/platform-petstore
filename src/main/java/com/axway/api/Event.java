package com.axway.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Basic model instance representing a PubSub event.
 */
public class Event {

    public static final String PET_CREATE_EVENT = "com.axway.pko.pet.create";
    public static final String PET_REMOVE_EVENT = "com.axway.pko.pet.remove";

    /**
     * The name of the event instance.
     */
    private String name;

    /**
     * The metadata payload associated with the event.
     */
    private ObjectNode data;

    /**
     * Constructs a new {@link Event} from a name and payload.
     *
     * @param name
     *      the name of the event to construct.
     * @param data
     *      the metadata payload to attach to the event.
     */
    public Event(@Nonnull String name, @Nonnull ObjectNode data) {
        this.name = Objects.requireNonNull(name);
        this.data = Objects.requireNonNull(data);
    }

    /**
     * Retrieves the name of the event instance.
     *
     * @return a {@link String} event name.
     */
    @JsonProperty
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the metadata payload of the event instance.
     *
     * @return a {@link ObjectNode} metadata payload.
     */
    @JsonProperty
    public ObjectNode getData() {
        return this.data;
    }
}
