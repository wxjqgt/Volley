package com.weibo.library.util;

import android.app.Application;

/**
 * Created by weibo on 17-4-14.
 */

public class App {

  public static final Application INSTANCE;

  static {
    Application app = null;
    try {
      app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
      if (app == null)
        throw new IllegalStateException("Static initialization of Applications must be on main thread.");
    } catch (final Exception e) {
      try {
        app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
      } catch (final Exception ex) {
        ex.printStackTrace();
      }
    } finally {
      INSTANCE = app;
    }
  }
}
