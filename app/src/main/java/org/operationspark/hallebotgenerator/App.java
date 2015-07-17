package org.operationspark.hallebotgenerator;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();

    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }
}
