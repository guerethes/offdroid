package br.com.guerethes.synchronization.webservice;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import br.com.guerethes.gson.Gson;
import br.com.guerethes.gson.GsonBuilder;

public class JSONProcessor {

	public synchronized static <T> List<T> toTransform(String json, Class<T> clazz) throws JSONException {
		Object obj = new JSONTokener(json).nextValue();
		if ( obj instanceof JSONObject ) {
			List<T> result = new ArrayList<T>();
			result.add(toObject(json, clazz));
			return result;
		} else if ( obj instanceof JSONArray ) {
			return toList(json, clazz);
		}
		return null;
	}
	
	public synchronized static <T> T toObject(String jsonText, Class<T> clazz) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.fromJson(jsonText, clazz);
	}

	public synchronized static String toJSON(Object object) {
		Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.toJson(object);
	}

	public synchronized static <T> List<T> toList(String jsonText, Class<T> clazz) throws JSONException {
		JSONArray jsonArray = new JSONArray(jsonText);
		List<T> result = new ArrayList<T>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			T object = toObject(jsonObject.toString(), clazz);
			result.add(object);
		}
		return result;
	}
	
}