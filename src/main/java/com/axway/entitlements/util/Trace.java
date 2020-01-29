package com.axway.entitlements.util;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The sole entry to the logging facilities.
 *
 * <p>Named Trace to help IDEs properly guess what we want, and also because we like the name.
 */
public class Trace {

  private static final Logger LOGGER = Logger.getLogger("com.axway");

  /**
   * We advocate for services to be able to dynamically change their logging level. <br>
   * (Although even if it's java-logging based, the actual code will differ from what is seen here.)
   */
  private static Level currLevel = Level.INFO;

  /** Suppressed instantiation (utility class). */
  private Trace() {}

  public static void info(String message) {
    // FIXME: if INFO enabled, then ... here and below
    LOGGER.log(currLevel, message);
  }

  public static void info(Object data) {
    info(Objects.toString(data));
  }

  public static void info(Throwable error) {
    LOGGER.log(currLevel, error, () -> "");
  }

  public static void info(String message, Throwable error) {
    LOGGER.log(currLevel, message, error);
  }
}
