package com.kitowcy.t_range;

import android.app.Application;

/**
 * Created by Łukasz Marczak
 *
 * @since 18.03.16
 */
public class App extends Application {
    public static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
