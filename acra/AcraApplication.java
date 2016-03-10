package br.com.guerethes.acra;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "")
public class AcraApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		ApplicationContextSingleton.initialize((Application) getApplicationContext());
	}
		
	public void customAppMethod() {
		ACRA.init(this);
		
		MailSender reportSender = new MailSender();
		ACRA.getErrorReporter().setReportSender(reportSender);
	}
	 
}