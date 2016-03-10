package br.com.guerethes.acra;

import android.app.Application;

public class ApplicationContextSingleton {

    private static Application app;
    
    public static void initialize(Application application) {
        app = application;
    }

    public static Application getApplicationContext() {
        return app;
    }
	
}