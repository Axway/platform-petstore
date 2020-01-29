package com.axway.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * Basic model instance representing a Pet.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pet {

    /**
     * A unique identifier used to represent the Pet instance.
     */
    private Optional<String> id;

    /**
     * A name used to represent the Pet instance.
     */
    private String name;

    /**
     * A type used to represent the Pet instance.
     */
    private String type;

    /**
     * A link to a photo of the Pet.
     */
    private URL photo;

    /**
     * Private constructor used for Jackson deserialization.
     */
    @SuppressWarnings("unused")
    private Pet() {
        this.id = Optional.empty();
    }

    /**
     * Constructs a pet instance.
     *
     * @param id
     *      an identifier used to represent the Pet instance.
     * @param name
     *      the name of the pet being created.
     * @param photo
     *      a link to a photo of the pet.
     * @param type
     *      a tag used to identify the photo type.
     */
    public Pet(@Nullable String id, @Nonnull String name, @Nonnull URL photo, @Nonnull String type) {
        this.id = Optional.ofNullable(id);
        this.name = Objects.requireNonNull(name);
        this.photo = Objects.requireNonNull(photo);
        this.type = Objects.requireNonNull(type);
    }

    /**
     * Retrieves the identifier of the Pet.
     *
     * @return a {@link String} identifier.
     */
    @JsonProperty
    public Optional<String> getId() {
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

    /**
     * Retrieves the type of the pet.
     *
     * @return a {@link String} type related to the pet.
     */
    @JsonProperty
    public String getType() {
        return this.type;
    }
}
