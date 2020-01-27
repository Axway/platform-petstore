package com.axway.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.Objects;

/**
 * Basic model instance representing a Pet.
 */
public class Pet {

    /**
     * A unique identifier used to represent the Pet instance.
     */
    private String id;

    /**
     * A name used to represent the Pet instance.
     */
    private String name;

    /**
     * A link to a photo of the Pet.
     */
    private URL photo;

    /**
     * Private constructor used for Jackson deserialization.
     */
    private Pet() { }

    /**
     * Constructs a pet instance.
     *
     * @param id
     *      an identifier used to represent the Pet instance.
     * @param name
     *      the name of the pet being created.
     * @param photo
     *      a link to a photo of the pet.
     */
    public Pet(@Nonnull String id, @Nonnull String name, @Nonnull URL photo) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.photo = Objects.requireNonNull(photo);
    }

    /**
     * Retrieves the identifier of the Pet.
     *
     * @return a {@link String} identifier.
     */
    @JsonProperty
    public String getId() {
        return this.id;
    }

    /**
     * Retrieves the identifier of the name.
     *
     * @return a {@link String} name.
     */
    @JsonProperty
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the photo URL of the Pet.
     *
     * @return a {@link URL} location of the pet photo.
     */
    @JsonProperty
    public URL getPhoto() {
        return this.photo;
    }
}
