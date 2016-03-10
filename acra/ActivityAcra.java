package br.com.guerethes.acra;

public class ActivityAcra {

	public static void comeca() {
		// Get the application instance
		AcraApplication app = (AcraApplication) ApplicationContextSingleton.getApplicationContext();
	    // Call a custom application method
	    app.customAppMethod();
	}

}