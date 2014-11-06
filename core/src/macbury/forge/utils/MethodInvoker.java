package macbury.forge.utils;

import java.lang.reflect.Method;

/**
 * Created by macbury on 06.11.14.
 */
public class MethodInvoker {

  public static Object invokeHandler(Object handler, String callback, boolean fallback, boolean report, Class<?>[] cls, Object... params) {
    return invokeHandler(handler, callback, fallback, report, cls, null, params);
  }

  public static Object invokeHandler(Object handler, String callback, boolean fallback, boolean report, Class<?>[] cls, Class<?>[] cls2, Object... params) {
    try {
      return invokeMethod(handler, callback, fallback, cls, cls2, params);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private static Object invokeMethod(Object handler, String callback,
                                     boolean fallback, Class<?>[] cls, Class<?>[] cls2, Object... params)
    throws Exception {

    if (handler == null || callback == null)
      return null;

    Method method = null;

    try {
      if (cls == null)
        cls = new Class[0];
      method = handler.getClass().getMethod(callback, cls);
      return method.invoke(handler, params);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    try {
      if (fallback) {

        if (cls2 == null) {
          method = handler.getClass().getMethod(callback);
          return method.invoke(handler);
        } else {
          method = handler.getClass().getMethod(callback, cls2);
          return method.invoke(handler, params);
        }

      }
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    return null;

  }
}
