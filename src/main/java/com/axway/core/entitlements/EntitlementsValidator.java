package com.axway.core.entitlements;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.axway.api.Pet;
import com.axway.client.entitlements.EntitlementsClient;
import com.axway.db.PetDAO;

/**
 * Validation class for checking entitlements against pet creation.
 *
 * This will check the current amount of created pets and only allow new pets to be
 * created if the entitlement has not been passed.
 */
public class EntitlementsValidator implements ConstraintValidator<Entitlements, Pet> {

    /**
     * The internal entitlements client to use when fetching limits.
     */
    private EntitlementsClient entitlements;

    /**
     * The DAO used to store and retrieve pet instances.
     */
    private PetDAO pets;

    /**
     * Creates a new validation instance with entitlements and pet storage.
     *
     * @param entitlements
     *      a client instance used to fetch entitlements from the service.
     * @param pets
     *      a pet storage instance used to retrieve pet counts.
     */
    public EntitlementsValidator(@Nonnull EntitlementsClient entitlements, @Nonnull PetDAO pets) {
        this.entitlements = Objects.requireNonNull(entitlements);
        this.pets = Objects.requireNonNull(pets);
    }

    /**
     * Determines whether the pet instance being created is within entitlement bounds.
     *
     * @param pet
     *      the pet instance the caller is trying to create.
     * @param context
     *      the request contest which may be used in validation.
     * @return
     *      true if the pet is able to be created (within entitlement bounds).
     */
    @Override
    public boolean isValid(Pet pet, ConstraintValidatorContext context) {
        try {
            // fetch the type and entitlements
            String type = pet.getType().toLowerCase();
            Map<String, Long> entitlements = this.entitlements.getEntitlements();

            // normalize the type value
            if (!type.endsWith("s")) {
                type += "s";
            }

            // if the type is unknown, it's allowed
            if (!entitlements.containsKey(type)) {
                return true;
            }

            // only pets below the limit can be created
            return this.pets.count(type) < entitlements.get(type);
        } catch (IOException e) {
            return false;
        }
    }
}
