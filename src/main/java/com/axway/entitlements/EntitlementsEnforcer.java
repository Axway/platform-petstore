package com.axway.entitlements;

import java.io.IOException;
import java.util.Set;

import com.axway.api.Pet;
import com.axway.db.PetDAO;
import com.axway.entitlements.model.Entitlement;

/** Ensures that the number of pets in store are within entitlements bounds. */
public class EntitlementsEnforcer {

  private final EntitlementsRequester entitlementsRequester;

  public EntitlementsEnforcer(EntitlementsConfiguration configuration) {
    entitlementsRequester = new EntitlementsRequester(configuration);
  }

  public boolean isAdditionAllowed(PetDAO dao, Pet pet) throws IOException {
    String type = pet.getType();
    long curr = count(type, dao);
    long entitled = count(type, entitlementsRequester.getEntitlements());
    return curr < entitled;
  }

  private long count(String petType, Set<Entitlement> entitlements) {
    long count = 0;
    for (Entitlement entitlement : entitlements) {
      if (entitlement.name.startsWith(petType)) {
        count += entitlement.intValue();
      }
    }
    return count;
  }

  private long count(String petType, PetDAO petDao) throws IOException {
    return petDao.get().filter(pet -> petType.equalsIgnoreCase(pet.getType())).count();
  }
}
