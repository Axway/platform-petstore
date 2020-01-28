package com.axway.db;

import com.axway.api.Pet;

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
    void create(Pet pet);

    /**
     * Retrieve all {@link Pet} instances.
     *
     * @return a {@link Stream} of {@link Pet} instances.
     */
    Stream<Pet> get();

    /**
     * Retrieve a specific {@link Pet} instance.
     *
     * @param id
     *      the identifier of the {@link Pet} to retrieve.
     * @return
     *      a potential {@link Pet} instance if found.
     */
    Optional<Pet> get(String id);

    /**
     * Removes a specific {@link Pet} instance.
     *
     * @param id
     *      the identifier of the {@link Pet} to remove.
     */
    void remove(String id);
}
