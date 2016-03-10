package br.com.guerethes.synchronization.webservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import android.annotation.SuppressLint;
import android.util.Log;
import br.com.guerethes.gson.JsonParser;
import br.com.guerethes.orm.engine.CriteryaSQLite;
import br.com.guerethes.orm.engine.EstrategiaURL;
import br.com.guerethes.orm.engine.criterya.pattern.ElementsQueryModel1;
import br.com.guerethes.orm.reflection.EntityReflection;
import br.com.guerethes.orm.reflection.FieldReflection;

@SuppressLint("DefaultLocale")
public class WebServiceImpl implements WebService {

	protected String url;
	protected EstrategiaURL estrategia;

	public WebServiceImpl() {
	}
	
	public WebServiceImpl(String url, EstrategiaURL estrategia) {
		this.url = url;
		setEstrategia(estrategia);
	}
	
	@Override
	public <T> List<T> get(CriteryaSQLite criteria) throws Exception {
		String[] json = new String[2];
		String completeUrl = "";
		
		for (int i = 0; i < criteria.getWheres().size(); i++) {
			ElementsQueryModel1 where = criteria.getWheres().get(i);
			if ( estrategia.isQuery() && criteria.isQuery() ){
				if ( where.getName().toString().contains(".") ) {
					Object clazz = null;
					String att = null;
					StringTokenizer token = new StringTokenizer(where.getName().toString(), ".");
					while (token.hasMoreElements()) {
						String attTemp =  (String) token.nextElement();
						if ( clazz == null ) {
							clazz = EntityReflection.getField(criteria.getClassEntity().newInstance(), attTemp);
						} else {
							clazz = FieldReflection.getValue(clazz, att);
						}
						att = attTemp;
					}
					completeUrl += FieldReflection.getColumnName(clazz.getClass(), att) + "=" + where.getValue().toString().replace("'", "");					
				} else {
					completeUrl += FieldReflection.getColumnName(criteria.getClassEntity(), where.getName().toString()) + "=" + where.getValue().toString().replace("'", "");
				}
				
				if ( i != (criteria.getWheres().size()-1) )
					completeUrl += "&";
			}
			else 
				completeUrl += where.getValue().toString().replace("'", "") + "/";
		}
		
		String pathEntity = EntityReflection.getPathName(criteria.getClassEntity());
		char inicio = (estrategia.isQuery() && criteria.isQuery()) ? '?' :  '/';
		HttpGet httpget = new HttpGet(url + pathEntity + inicio + completeUrl);
		return get(criteria.getClassEntity(), json, httpget);
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings({ "unchecked" })
	private <T> List<T> get(Class<?> classe, String[] json, HttpGet httpget) throws Exception {
		HttpResponse response;
		getHttpGet(httpget);
		System.out.println(httpget.getURI());
		response = HttpClientSingleton.getHttpClientInstace().execute(httpget);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// Array de String que recebe o JSON do Web Service
			json[0] = String.valueOf(response.getStatusLine().getStatusCode());
			InputStream instream = entity.getContent();
			json[1] = toString(instream);
			instream.close();

			if (json[0].equals("200")) {
				return (ArrayList<T>) JSONProcessor.toTransform(json[1], classe);
			} else {
				throw new Exception(json[1]);
			}
		}				
		return null;
	}

	@SuppressWarnings({ "unchecked", "unused"})
	@Override
	public <T> T post(T entity) throws Exception {
		String[] result = new String[2];
		HttpPost httpPost = new HttpPost(new URI(url + EntityReflection.getPathName(entity.getClass())+ "/"));
		getHttpPost(httpPost);
		StringEntity sEntity = new StringEntity(JSONProcessor.toJSON(entity));
		httpPost.setEntity(sEntity);
	
		HttpResponse response;
		response = HttpClientSingleton.getHttpClientInstace().execute(httpPost);
		HttpEntity httpEntity = response.getEntity();
		
	
		if (entity != null) {
			result[0] = String.valueOf(response.getStatusLine().getStatusCode());
			InputStream instream = httpEntity.getContent();
			result[1] = toString(instream);
			
			instream.close();
			Log.d("post", "Result from post JsonPost : " + result[0] + " : " + result[1]);
			
			if (result[0].equals("201")) {
				return (!result[1].isEmpty() && !result[1].equals("")) ? ((T) JSONProcessor.toObject(result[1], entity.getClass())) : entity;
			} else {
				throw new Exception(result[1]);
			}
		}
		return null;
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public <T> T put(T entity) throws Exception {
		String[] result = new String[2];
		HttpPut httpPut = new HttpPut(new URI(url + entity.getClass().getSimpleName().toLowerCase() + "/"));
		getHttpPut(httpPut);
		StringEntity sEntity = new StringEntity(JSONProcessor.toJSON(entity));
		httpPut.setEntity(sEntity);

		HttpResponse response;
		response = HttpClientSingleton.getHttpClientInstace().execute(httpPut);
		HttpEntity httpEntity = response.getEntity();

		if (entity != null) {
			result[0] = String.valueOf(response.getStatusLine().getStatusCode());
			InputStream instream = httpEntity.getContent();
			result[1] = toString(instream);
			instream.close();
			Log.d("put", "Result from put JsonPost : " + result[0] + " : " + result[1]);
			
			if (result[0].equals("200")) {
				JsonParser parser = new JsonParser();
				return ((T) JSONProcessor.toObject(result[1], entity.getClass()));
			} else {
				throw new Exception(result[1]);
			}		
		}
		return null;
	}

	@SuppressLint("NewApi")
	@Override
	public void delete(Object entity, int id) throws Exception {
		String[] result = new String[2];
		HttpDelete httpDelete = new HttpDelete(new URI(url + entity.getClass().getSimpleName().toLowerCase() + "/" + id));
		getHttpDelete(httpDelete);
		HttpResponse response;
		response = HttpClientSingleton.getHttpClientInstace().execute(httpDelete);
		HttpEntity httpEntity = response.getEntity();

		if (entity != null) {
			result[0] = String.valueOf(response.getStatusLine().getStatusCode());
			InputStream instream = httpEntity.getContent();
			result[1] = toString(instream);
			instream.close();
			if (result[0].equals("200")) {
				Log.d("delete", "Result from delete: " + result[0] + " : " + result[1]);	
			} else {
				throw new Exception(result[1]);
			}
		}
	}

	@Override
	public <T> T login(CriteryaSQLite criteria) throws Exception {
		return null;
	}
	
	protected String toString(InputStream is) throws IOException {
		byte[] bytes = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int lidos;
		while ((lidos = is.read(bytes)) > 0) {
			baos.write(bytes, 0, lidos);
		}
		return new String(baos.toByteArray());
	}

	@SuppressLint("NewApi")
	protected void getHttpGet(HttpGet httpget) {
		
	}

	@SuppressLint("NewApi")
	protected void getHttpPost(HttpPost httpPost) {
		
	}

	@SuppressLint("NewApi")
	protected void getHttpPut(HttpPut httpPut) {
		
	}
	
	@SuppressLint("NewApi")
	protected void getHttpDelete(HttpDelete httpDelete) {
		
	}

	public EstrategiaURL getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(EstrategiaURL estrategia) {
		this.estrategia = estrategia;
	}
	
}