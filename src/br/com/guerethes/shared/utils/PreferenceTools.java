package br.com.guerethes.shared.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("NewApi")
public class PreferenceTools {

    private static SharedPreferences sharedPreferences;
    private static final String PREFS_PRIVATE = "PREFS_PRIVATE";

    // Limpar preferencias do smartphone
    public static void cleanPreferences(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }
    
    // Limpar uma preferencia do smartphone
    public static void cleanOnePreference(String key, Context context){
    	 SharedPreferences settings = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
         settings.edit().remove(key).commit();
    }

    // Salva as preferencias na memoria local
    public static void setPreferencias(Object obj, String key, boolean clear, Context context) {
        if(clear) {
            SharedPreferences settings = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
            settings.edit().remove(key).commit();
        }

        sharedPreferences = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsPrivateEditor = sharedPreferences.edit();
        prefsPrivateEditor.putString(key, obj + getPreferencias(key, context));

        prefsPrivateEditor.commit();
    }
    
    // Salva as preferencias na memoria local
    public static void setPreferencias(Object[] obj, String key, boolean clear, Context context) {
    	if(clear) {
    		SharedPreferences settings = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
    		settings.edit().remove(key).commit();
    	}
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < obj.length; i++) {
    	    sb.append(obj[i]).append(",");
    	}    	
    	sharedPreferences = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
    	SharedPreferences.Editor prefsPrivateEditor = sharedPreferences.edit();
    	prefsPrivateEditor.putString(key, sb.toString() + getPreferencias(key, context));
    	
    	prefsPrivateEditor.commit();
    }
    
    // retorna as preferencias na memoria local
    public static String getPreferencias (String key, Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        String retorno = sharedPreferences.getString(key, "");
        sharedPreferences = null;

        if( retorno == null || retorno.equalsIgnoreCase("") )
            return "";
        else
            return retorno;
    }
  
    // retorna as preferencias na memoria local
    public static Object[] getPreferenciasArray (String key, Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        Object[] retorno = sharedPreferences.getString(key, "").split(",");
        sharedPreferences = null;
        return retorno;
    }
    
}