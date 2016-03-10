package br.com.guerethes.shared.utils;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class DeviceInformationUtils {

	@SuppressWarnings("unused")
	private static String PATH_EXCEPTION_LOGS = null;

	private static String APP_VERSION = "unknown";
	private static String PHONE_MODEL = "unknown";
	private static String ANDROID_VERSION = "unknown";
	private static String SCREEN_HEIGHT = "unknown";
	private static String SCREEN_WIDTH = "unknown";
	private static String SCREEN_DPI = "unknown";

	/**
	 * Retorna os dados coletados do aparelho em formato de String.
	 * 
	 * @return
	 */
	public static String getDeviceInfo(Context context) {
		register(context);
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
	
	public static String getAppName(Context context) {
	    int stringId = context.getApplicationInfo().labelRes;
	    return context.getString(stringId);
	}
	
	/**
	 * Registra o {@link DefaultExceptionHandler} como
	 * {@link UncaughtExceptionHandler} padr�o da aplica��o.
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return String.valueOf(pi.versionCode);
		} catch (NameNotFoundException e) {
			Log.e(DeviceInformationUtils.class.getName(), e.getMessage(), e);
		}
		return "-1";
	}
	
	public static String getPackage(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return String.valueOf(pi.packageName);
		} catch (NameNotFoundException e) {
			Log.e(DeviceInformationUtils.class.getName(), e.getMessage(), e);
		}
		return "-1";
	}
	
	/**
	 * Registra o {@link DefaultExceptionHandler} como
	 * {@link UncaughtExceptionHandler} padr�o da aplica��o.
	 * 
	 * @param context
	 * @return
	 */
	private static void register(Context context) {
		PackageManager pm = context.getPackageManager();

		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(metrics);

			// Armazenando dados que ser�o utilizados para log das exce��es
			APP_VERSION = pi.versionName;
			PHONE_MODEL = android.os.Build.MODEL;
			SCREEN_HEIGHT = String.valueOf(metrics.heightPixels);
			SCREEN_WIDTH = String.valueOf(metrics.widthPixels);
			SCREEN_DPI = String.valueOf(metrics.densityDpi);
			ANDROID_VERSION = android.os.Build.VERSION.RELEASE;

		} catch (NameNotFoundException e) {

			Log.e(DeviceInformationUtils.class.getName(), e.getMessage(), e);

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
