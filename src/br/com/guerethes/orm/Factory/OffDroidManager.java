package br.com.guerethes.orm.Factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import br.com.guerethes.acra.ActivityAcra;
import br.com.guerethes.acra.DeviceInformationHandler;
import br.com.guerethes.exception.OffDroidException;
import br.com.guerethes.orm.annotation.execute.AfterInsert;
import br.com.guerethes.orm.annotation.execute.AfterRemove;
import br.com.guerethes.orm.annotation.execute.AfterUpdate;
import br.com.guerethes.orm.annotation.execute.BeforeInsert;
import br.com.guerethes.orm.annotation.execute.BeforeRemove;
import br.com.guerethes.orm.annotation.execute.BeforeUpdate;
import br.com.guerethes.orm.engine.CriteryaSQLite;
import br.com.guerethes.orm.engine.EstrategiaAtualizacaoBD;
import br.com.guerethes.orm.engine.criterya.QuerySample;
import br.com.guerethes.orm.engine.criterya.Restriction;
import br.com.guerethes.orm.engine.i.IPersistenceManager;
import br.com.guerethes.orm.engine.i.PersistDB;
import br.com.guerethes.orm.enumeration.ModelBeavior;
import br.com.guerethes.orm.reflection.EntityReflection;
import br.com.guerethes.orm.reflection.MethodReflection;
import br.com.guerethes.sync.utils.SyncUtils;
import br.com.guerethes.synchronization.annotation.OnlyLocalStorage;
import br.com.guerethes.synchronization.annotation.OnlyOnLine;
import br.com.guerethes.synchronization.networkUtils.NetWorkUtils;
import br.com.guerethes.synchronization.networkUtils.NetworkChangeReceiver;
import br.com.guerethes.synchronization.webservice.WebService;

public class OffDroidManager extends Activity {

	private static IPersistenceManager pm = null;
	public static WebService service = null;
	private static Collection<String> classes;
	
	public static boolean isReady(String bd) {
		boolean ready = ( pm == null || !pm.getDataBaseName().equals(bd) );
		if ( ready && pm != null )
			pm.closeBd();
		return ready;
	}
	
	public static void createOffLineManager(Context context, WebService web, IPersistenceManager persistence, String email) throws Exception{
		//Classe respons�vel pela sincroniza��o dos dados
		if ( web == null )
			throw new Exception("It is necessary to pass an instance of the WebService class.");
		if ( persistence == null )
			throw new Exception("It is necessary to pass an instance of the IPersistenceManager class.");

		service = (WebService) web;
		pm = persistence;
		NetWorkUtils.context = context;
		
		initializeBroadcast(context);
		
		if(email != null && !email.isEmpty()){
			DeviceInformationHandler.register(context);
			ActivityAcra.comeca(email);
		}
	}
	
	public static void createOffLineManager(Context context, String dbName, List<String> classes, WebService web, IPersistenceManager persistence, String email) throws Exception{
		//Classe respons�vel pela sincroniza��o dos dados
		if ( web == null )
			throw new Exception("It is necessary to pass an instance of the WebService class.");
		if ( persistence == null )
			throw new Exception("It is necessary to pass an instance of the IPersistenceManager class.");
		
		if ( classes == null ) 
			classes = new ArrayList<String>();
		classes.add("br.com.guerethes.orm.model.Sincronizacao");
		service = (WebService) web;
		NetWorkUtils.context = context;
		OffDroidManager.classes = new ArrayList<String>(classes);
		pm = persistence;
		
		initializeBroadcast(context);
		
		if(email != null && !email.isEmpty()){
			DeviceInformationHandler.register(context);
			ActivityAcra.comeca(email);
		}
	}

	public static void createOffLineManager(Context context, String dbName, List<String> classes, WebService web, String email) throws Exception{
		createOffLineManager(context, dbName, classes, web, email, null);
	}
	
	public static void createOffLineManager(Context context, String dbName, List<String> classes, WebService web, EstrategiaAtualizacaoBD estrategia) throws Exception{
		createOffLineManager(context, dbName, classes, web, null, estrategia);
	}
	
	public static void createOffLineManager(Context context, String dbName, List<String> classes, WebService web, String email, EstrategiaAtualizacaoBD estrategia) throws Exception{
		if ( web == null )
			throw new Exception("It is necessary to pass an instance of the WebService class.");

		if ( classes == null ) 
			classes = new ArrayList<String>();
		classes.add("br.com.guerethes.orm.model.Sincronizacao");
		
		service = (WebService) web;
		NetWorkUtils.context = context;
		OffDroidManager.classes = new ArrayList<String>(classes);
		DeviceInformationHandler.register(context);
		
	    initializeBroadcast(context);
	    
		pm = new PersistenceManager(context, dbName, DeviceInformationHandler.APP_CODE, classes, Context.MODE_PRIVATE, ModelBeavior.RENEW, 1, estrategia);
		
		if(email != null && !email.isEmpty()) {
			ActivityAcra.comeca(email);
		}
	}

	private static void initializeBroadcast(Context context) {
		IntentFilter mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
	    mIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
	    context.registerReceiver(new NetworkChangeReceiver(), mIntentFilter);
	    NetWorkUtils.setOnline(NetWorkUtils.isOnline(context));
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static Object find(final CriteryaSQLite criteria, final boolean file) throws Exception {
		if ( file ) {
			return service.getFile(criteria);
		} else {
			if ( criteria.isLocal() ) {
				Map<String, String> mapResult = new QuerySample(criteria.getClassEntity(), criteria.getWheres(), 
						criteria.getOrderBy(), criteria.getLimit()).toSqlMap();
				return pm.findAllOtimizado(mapResult.get("select"), criteria.getClassEntity(), mapResult);
			} else {
				if ( EntityReflection.haAnnotation(criteria.getClassEntity(), OnlyOnLine.class) ) {
					if ( NetWorkUtils.isOnline() ) {
						return service.get(criteria);
					} else {
						return null;
					}
				} else if ( EntityReflection.haAnnotation(criteria.getClassEntity(), OnlyLocalStorage.class) ) {
					Map<String, String> mapResult = new QuerySample(criteria.getClassEntity(), criteria.getWheres(), 
							criteria.getOrderBy(), criteria.getLimit()).toSqlMap();
					return pm.findAllOtimizado(mapResult.get("select"), criteria.getClassEntity(), mapResult);
				} else {
					if ( NetWorkUtils.isOnline() ) {
						final List result = service.get(criteria);
						SyncUtils.syncAll(result, criteria.getClassEntity());
						return result;	
					} else {
						Map<String, String> mapResult = new QuerySample(criteria.getClassEntity(), criteria.getWheres(), 
								criteria.getOrderBy(), criteria.getLimit()).toSqlMap();
						return pm.findAllOtimizado(mapResult.get("select"), criteria.getClassEntity(), mapResult);
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<PersistDB> insertAll(List entitys) throws Exception {
		if ( entitys != null && entitys.size() > 0 ) {
			for (int i = 0; i < entitys.size(); i++) {
				PersistDB entity = (PersistDB) entitys.get(i);
				insert(entity);
			}
		}
		return entitys;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<PersistDB> insertAllLocal(List entitys) throws Exception {
		if ( EntityReflection.haAnnotation(entitys.get(0).getClass(), OnlyOnLine.class) ) {
			throw new OffDroidException("Não foi possível inserir devido a anotação presente na classe.");
		} else {
			if ( entitys != null && entitys.size() > 0 ) {
				for (int i = 0; i < entitys.size(); i++) {
					PersistDB entity = (PersistDB) entitys.get(i);
					insertLocal(entity);
				}
			}
		}
		return entitys;
	}
	
	public static <T> PersistDB insertLocal(PersistDB entity) throws Exception {
		if ( EntityReflection.haAnnotation(entity.getClass(), OnlyOnLine.class) ) {
			throw new OffDroidException("Não foi possível inserir devido a anotação presente na classe.");
		} else {
			if ( MethodReflection.haAnnotation(entity.getClass(), BeforeInsert.class) )
				MethodReflection.getMethodAnnotation(entity.getClass(), BeforeInsert.class).invoke(entity);
			entity = pm.insert(entity);
			if ( MethodReflection.haAnnotation(entity.getClass(), AfterInsert.class) )
				MethodReflection.getMethodAnnotation(entity.getClass(), AfterInsert.class).invoke(entity);
			return entity;
		}
	}
	
	public static <T> PersistDB insert(PersistDB entity) throws Exception {
		Object obj = null;
		
		if ( MethodReflection.haAnnotation(entity.getClass(), BeforeInsert.class) )
			MethodReflection.getMethodAnnotation(entity.getClass(), BeforeInsert.class).invoke(entity);
		
		if ( NetWorkUtils.isOnline() && !(EntityReflection.haAnnotation(entity.getClass(), OnlyLocalStorage.class)))
			entity = (PersistDB) service.post(entity);
		entity = (PersistDB) (obj != null ? obj : entity);
		if ( !(EntityReflection.haAnnotation(entity.getClass(), OnlyOnLine.class)) )
			entity = pm.insert(entity);

		if ( MethodReflection.haAnnotation(entity.getClass(), AfterInsert.class) )
			MethodReflection.getMethodAnnotation(entity.getClass(), AfterInsert.class).invoke(entity);

		return entity;
	}

	public static <T> PersistDB updateLocal(PersistDB entity) throws Exception {
		if ( !(EntityReflection.haAnnotation(entity.getClass(), OnlyOnLine.class)) ) {
			if ( MethodReflection.haAnnotation(entity.getClass(), BeforeUpdate.class) )
				MethodReflection.getMethodAnnotation(entity.getClass(), BeforeUpdate.class).invoke(entity);
			pm.update(entity);
			
			if ( MethodReflection.haAnnotation(entity.getClass(), AfterUpdate.class) )
				MethodReflection.getMethodAnnotation(entity.getClass(), AfterUpdate.class).invoke(entity);
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> PersistDB update(PersistDB entity) throws Exception {
		Object obj = null;
		
		if ( MethodReflection.haAnnotation(entity.getClass(), BeforeUpdate.class) )
			MethodReflection.getMethodAnnotation(entity.getClass(), BeforeUpdate.class).invoke(entity);
		
		if ( NetWorkUtils.isOnline() && !(EntityReflection.haAnnotation(entity.getClass(), OnlyLocalStorage.class)))
			obj = (T) service.put(entity);
		entity = (PersistDB) (obj != null ? obj : entity);
		if ( !(EntityReflection.haAnnotation(entity.getClass(), OnlyOnLine.class)) )
			pm.update(entity);
		
		if ( MethodReflection.haAnnotation(entity.getClass(), AfterUpdate.class) )
			MethodReflection.getMethodAnnotation(entity.getClass(), AfterUpdate.class).invoke(entity);
		
		return entity;
	}

	public static void deleteLocal(PersistDB entity) throws Exception{
		if ( !(EntityReflection.haAnnotation(entity.getClass(), OnlyOnLine.class)) ) {
			if ( MethodReflection.haAnnotation(entity.getClass(), BeforeRemove.class) )
				MethodReflection.getMethodAnnotation(entity.getClass(), BeforeRemove.class).invoke(entity);
			pm.remove(entity);
			if ( MethodReflection.haAnnotation(entity.getClass(), AfterRemove.class) )
				MethodReflection.getMethodAnnotation(entity.getClass(), AfterRemove.class).invoke(entity);
		}
	}
	
	public static void delete(PersistDB entity) throws Exception{
		if ( MethodReflection.haAnnotation(entity.getClass(), BeforeRemove.class) )
			MethodReflection.getMethodAnnotation(entity.getClass(), BeforeRemove.class).invoke(entity);

		if ( NetWorkUtils.isOnline() && !(EntityReflection.haAnnotation(entity.getClass(), OnlyLocalStorage.class)))
			service.delete(entity, (Integer) EntityReflection.getID(entity));
		
		pm.remove(entity);

		if ( MethodReflection.haAnnotation(entity.getClass(), AfterRemove.class) )
			MethodReflection.getMethodAnnotation(entity.getClass(), AfterRemove.class).invoke(entity);
	}

	public static void executeNativeSQL(String sql) throws Exception{
		pm.executeNativeSql(sql);
	}
	
	public static void clearAllBD() throws Exception {
		if ( classes != null && !classes.isEmpty() ) {
			for (String tabela : classes)
				pm.executeNativeSql("DROP TABLE IF EXISTS " + 
						EntityReflection.getTableName(Class.forName(tabela.trim())) + ";");	
		}
	}
	
	public static Object login(CriteryaSQLite criteria) throws Exception {
		if ( criteria.isLocal() ) {
			criteria.add(Restriction.limit("1"));
			Map<String, String> mapResult = new QuerySample(criteria.getClassEntity(), criteria.getWheres(), 
					criteria.getOrderBy(), criteria.getLimit()).toSqlMap();
			List<?> list = pm.findAllOtimizado(mapResult.get("select"), criteria.getClassEntity(), mapResult);
			if ( list != null && list.size() > 0 )
				return list.get(0);
			else
				return null;
		} else {
			if ( EntityReflection.haAnnotation(criteria.getClassEntity(), OnlyOnLine.class) ) {
				if ( NetWorkUtils.isOnline() ) {
					return service.login(criteria);
				} else {
					return null;
				}
			} else if ( EntityReflection.haAnnotation(criteria.getClassEntity(), OnlyLocalStorage.class) ) {
				criteria.add(Restriction.limit("1"));
				Map<String, String> mapResult = new QuerySample(criteria.getClassEntity(), criteria.getWheres(), 
						criteria.getOrderBy(), criteria.getLimit()).toSqlMap();
				List<?> list = pm.findAllOtimizado(mapResult.get("select"), criteria.getClassEntity(), mapResult);
				if ( list != null && list.size() > 0 )
					return list.get(0);
				else
					return null;
			} else {
				if ( NetWorkUtils.isOnline() ) {
					return service.login(criteria);
				} else {
					criteria.add(Restriction.limit("1"));
					Map<String, String> mapResult = new QuerySample(criteria.getClassEntity(), criteria.getWheres(), 
							criteria.getOrderBy(), criteria.getLimit()).toSqlMap();
					List<?> list = pm.findAllOtimizado(mapResult.get("select"), criteria.getClassEntity(), mapResult);
					if ( list != null && list.size() > 0 )
						return list.get(0);
					else
						return null;
				}
			}
		}
	}
	
}