package com.axway.entitlements.util;

import java.lang.reflect.Field;

/**
 * Container of static utility methods for creating {@code toString()} representations of objects.
 */
public class ToString {

  /** Suppressed instantiation (utility class). */
  private ToString() {}

  /**
   * Constructs a {@code toString()} representation of the specified {@code instance}, based on the
   * public fields declared by the specified {@code type}, in terms of their names and values.
   *
   * @param <T> helps the type system do its static checking regarding {@code instance, type}.
   * @param type whose public fields to consider
   * @param instance to read field values from
   * @param fieldDelimiter non-null, although null won't break your program. Handle if you borrow
   *     this code.
   * @return non-{@code null}.
   */
  public static <T> String fromPublicFields(
      Class<? super T> type, T instance, String fieldDelimiter) {
    StringBuilder buf = new StringBuilder();

    for (Field field : type.getFields()) {
      Object value;

      try {
        value = field.get(instance);
      } catch (IllegalAccessException e) {
        // just fail fast, this code is a presentation aid.
        throw new RuntimeException(e);
      }

      buf.append(field.getName()).append(':').append(value).append(fieldDelimiter);
    }

    return buf.toString();
  }

  /**
   * Equivalent to {@code fromPublicFields(type, instance, "")}, i.e. uses the empty string as field
   * delimiter.
   */
  public static <T> String fromPublicFields(Class<? super T> type, T instance) {
    return fromPublicFields(type, instance, "");
  }
}
