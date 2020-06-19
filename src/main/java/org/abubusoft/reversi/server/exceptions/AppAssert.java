package org.abubusoft.reversi.server.exceptions;

public abstract class AppAssert {
  private AppAssert() {

  }

  public static void assertTrue(boolean condition, Class<? extends AppRuntimeException> exceptionClazz, String message, Object... params) {
    if (!condition) {
      AppRuntimeException exception;
      try {
        exception = exceptionClazz.getConstructor(String.class).newInstance(String.format(message, params));
      } catch (Exception e) {
        exception = new AppRuntimeException(String.format("Can not instantiate %s for: %s", exceptionClazz.getSimpleName(), String.format(message, params)));
      }
      throw exception;
    }
  }

  public static void fail(Class<? extends AppRuntimeException> exceptionClazz, String message, Object... params) {
    assertTrue(false, exceptionClazz, message, params);
  }

  public static void fail(String message, Object... params) {
    assertTrue(false, AppRuntimeException.class, message, params);
  }
}
