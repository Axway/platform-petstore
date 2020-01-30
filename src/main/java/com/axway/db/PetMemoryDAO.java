package com.axway.db;

import com.axway.api.Pet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Memory storage class for Pet instances.
 *
 * This can be seen as a mock implementation for testing purposes.
 */
public class PetMemoryDAO implements PetDAO {

    /**
     * An internal mapping of Pet instances.
     */
    private final Map<String, Pet> pets;

    /**
     * Constructs a new storage class.
     */
    public PetMemoryDAO() {
        this.pets = new HashMap<>();
    }

    /**
     * Create a new {@link Pet} instance.
     *
     * @param pet
     *      the {@link Pet} instance to store.
     */
    public void create(Pet pet) {
        if (!pet.getId().isPresent()) {
            pet = new Pet(
                UUID.randomUUID().toString(),
                pet.getName(),
                pet.getPhoto(),
                pet.getType()
            );
        }
        this.pets.put(pet.getId().get(), pet);
    }

    /**
     * Retrieve all {@link Pet} instances.
     *
     * @return a {@link Stream} of {@link Pet} instances.
     */
    public Stream<Pet> get() {
        return this.pets.values().stream();
    }

    /**
     * Retrieve a specific {@link Pet} instance.
     *
     * @param id
     *      the identifier of the {@link Pet} to retrieve.
     * @return
     *      a potential {@link Pet} instance if found.
     */
    public Optional<Pet> get(String id) {
        return Optional.ofNullable(this.pets.get(id));
    }

    /**
     * Removes a {@link Pet} instance.
     *
     * @param id
     *      the identifier of the {@link Pet} to retrieve.
     */
    public void remove(String id) {
        this.pets.remove(id);
    }
}
