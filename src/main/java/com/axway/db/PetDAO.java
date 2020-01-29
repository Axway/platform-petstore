package com.axway.db;

import com.axway.api.Pet;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Storage interface for Pet instances.
 */
public interface PetDAO {

    /**
     * Create a new {@link Pet} instance.
     *
     * @param pet
     *      the {@link Pet} instance to store.
     */
    void create(Pet pet) throws IOException;

    /**
     * Retrieve all {@link Pet} instances.
     *
     * @return a {@link Stream} of {@link Pet} instances.
     */
    Stream<Pet> get() throws IOException;

    /**
     * Retrieve a specific {@link Pet} instance.
     *
     * @param id
     *      the identifier of the {@link Pet} to retrieve.
     * @return
     *      a potential {@link Pet} instance if found.
     */
    Optional<Pet> get(String id) throws IOException;

    /**
     * Removes a specific {@link Pet} instance.
     *
     * @param id
     *      the identifier of the {@link Pet} to remove.
     */
    void remove(String id) throws IOException;
}
