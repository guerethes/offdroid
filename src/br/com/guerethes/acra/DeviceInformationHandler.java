package br.com.guerethes.acra;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class DeviceInformationHandler {

	private static String PATH_EXCEPTION_LOGS = null;

	public static String APP_VERSION = "unknown";
	private static String PHONE_MODEL = "unknown";
	private static String ANDROID_VERSION = "unknown";
	private static String SCREEN_HEIGHT = "unknown";
	private static String SCREEN_WIDTH = "unknown";
	private static String SCREEN_DPI = "unknown";
	public static String APP_NAME = "unknown";
	public static int APP_CODE = 0;
	public static String PACKAGE_NAME = "unknown";

	/**
	 * Retorna os dados coletados do aparelho em formato de String.
	 * 
	 * @return
	 */
	public static String getDeviceInfo() {
		StringBuilder sb = new StringBuilder();

		sb.append("V:");
		sb.append(ANDROID_VERSION);
		sb.append("|APP:");
		sb.append(APP_VERSION);
		sb.append("|MODEL:");
		sb.append(PHONE_MODEL);
		sb.append("|H:");
		sb.append(SCREEN_HEIGHT);
		sb.append("|W:");
		sb.append(SCREEN_WIDTH);
		sb.append("|D:");
		sb.append(SCREEN_DPI);

		return sb.toString();
	}

	public static String getExceptionsDir() {

		return PATH_EXCEPTION_LOGS;
	}
	
	/**
	 * Registra o {@link DefaultExceptionHandler} como
	 * {@link UncaughtExceptionHandler} padr�o da aplica��o.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean register(Context context) {
		// Recuperando UncaughtExceptionHandler definido para a aplica��o
		// final UncaughtExceptionHandler currentHandler =
		// Thread.getDefaultUncaughtExceptionHandler();

		// Se o handler j� foi definido anteriormente, o processamento da classe
		// � desnecess�rio
		// TODO Verificar a utilidade disso
		// if (currentHandler instanceof DefaultExceptionHandler)
		// return false;

		PackageManager pm = context.getPackageManager();

		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(metrics);

			// Armazenando dados que ser�o utilizados para log das exce��es
			APP_CODE = pi.versionCode;
			APP_VERSION = pi.versionName;
			PACKAGE_NAME = pi.packageName;
			PHONE_MODEL = android.os.Build.MODEL;
			SCREEN_HEIGHT = String.valueOf(metrics.heightPixels);
			SCREEN_WIDTH = String.valueOf(metrics.widthPixels);
			SCREEN_DPI = String.valueOf(metrics.densityDpi);
			ANDROID_VERSION = android.os.Build.VERSION.RELEASE;
			APP_NAME = context.getString(context.getApplicationInfo().labelRes);

			setupExceptionsDir(context);

		} catch (NameNotFoundException e) {

			Log.e(DeviceInformationHandler.class.getName(), e.getMessage(), e);

		}
		return true;
	}
	
	private static void setupExceptionsDir(Context context) {
		try {

			File exceptionDir = null;
			
			File cacheDir = context.getCacheDir();

			if (cacheDir != null) {
				exceptionDir = new File(cacheDir, "exception");
			}
			else{
				File appDir = context.getFilesDir();

				if (appDir != null) {
					exceptionDir = new File(appDir, "exception");
				}
			}

			if (!exceptionDir.exists()) {
				exceptionDir.mkdir();
			}

			PATH_EXCEPTION_LOGS = exceptionDir.getAbsolutePath();

		} catch (Exception e) {
			PATH_EXCEPTION_LOGS = context.getCacheDir().getAbsolutePath();
		}
	}
	
	public static String getDefaultUserAgent() {
	    StringBuilder result = new StringBuilder(64);
	    result.append("Dalvik/");
	    result.append(System.getProperty("java.vm.version")); // such as 1.1.0
	    result.append(" (Linux; U; Android ");

	    String version = Build.VERSION.RELEASE; // "1.0" or "3.4b5"
	    result.append(version.length() > 0 ? version : "1.0");

	    // add the model for the release build
	    if ("REL".equals(Build.VERSION.CODENAME)) {
	        String model = Build.MODEL;
	        if (model.length() > 0) {
	            result.append("; ");
	            result.append(model);
	        }
	    }
	    String id = Build.ID; // "MASTER" or "M4-rc20"
	    if (id.length() > 0) {
	        result.append(" Build/");
	        result.append(id);
	    }
	    result.append(")");
	    return result.toString();
	}   
}
