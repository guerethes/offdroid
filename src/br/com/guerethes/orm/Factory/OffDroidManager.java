package br.com.guerethes.orm.Factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import br.com.guerethes.acra.ActivityAcra;
import br.com.guerethes.acra.DeviceInformationHandler;
import br.com.guerethes.exception.OffDroidException;
import br.com.guerethes.orm.annotation.ddl.ManyToOne;
import br.com.guerethes.orm.engine.CriteryaSQLite;
import br.com.guerethes.orm.engine.EstrategiaAtualizacaoBD;
import br.com.guerethes.orm.engine.criterya.QuerySample;
import br.com.guerethes.orm.engine.i.IPersistenceManager;
import br.com.guerethes.orm.engine.i.PersistDB;
import br.com.guerethes.orm.enumeration.ModelBeavior;
import br.com.guerethes.orm.model.Sincronizacao;
import br.com.guerethes.orm.reflection.EntityReflection;
import br.com.guerethes.orm.reflection.FieldReflection;
import br.com.guerethes.synchronization.annotation.OnlyLocalStorage;
import br.com.guerethes.synchronization.annotation.OnlyOnLine;
import br.com.guerethes.synchronization.networkUtils.NetWorkUtils;
import br.com.guerethes.synchronization.networkUtils.NetworkChangeReceiver;
import br.com.guerethes.synchronization.webservice.WebService;

public class OffDroidManager extends Activity {

	private static IPersistenceManager pm = null;
	private static WebService service = null;
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
	    NetWorkUtils.setOnline(NetworkChangeReceiver.isOnline(context));
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static Object find(final CriteryaSQLite criteria) throws Exception {
		if ( criteria.isLocal() ) {
			return pm.findAll(new QuerySample(criteria.getClassEntity(), criteria.getWheres(), 
						criteria.getOrderBy(), criteria.getLimit()).toSql(), criteria.getClassEntity());
		} else {
			if ( EntityReflection.haAnnotation(criteria.getClassEntity(), OnlyOnLine.class) ) {
				if ( NetWorkUtils.isOnline() ) {
					return service.get(criteria);
				} else {
					return null;
				}
			} else if ( EntityReflection.haAnnotation(criteria.getClassEntity(), OnlyLocalStorage.class) ) {
				return pm.findAll(new QuerySample(criteria.getClassEntity(), criteria.getWheres(), criteria.getOrderBy(), criteria.getLimit()).toSql(), criteria.getClassEntity());
			} else {
				if ( NetWorkUtils.isOnline() ) {
					final List result = service.get(criteria);
					new Thread(new Runnable() { 
			            public void run(){        
			            	try {
								syncAll(result, criteria.getClassEntity());
							} catch (Exception e) {
								e.printStackTrace();
							}
			            }
			        }).start();
					return result;	
				} else {
					return pm.findAll(new QuerySample(criteria.getClassEntity(), criteria.getWheres(), criteria.getOrderBy(), criteria.getLimit()).toSql(), criteria.getClassEntity());
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void syncAll(List<?> array, Class classe) throws Exception {
		ArrayList<?> all = new ArrayList(array);
		if ( all != null && !all.isEmpty() ) {
			ArrayList arrayBD = (ArrayList) pm.findAll(classe);
			if ( arrayBD != null && !arrayBD.isEmpty() ) {
				all.removeAll(arrayBD);
				if ( !all.isEmpty() )
					pm.insertAll(all);	
			} else
				pm.insertAll(all);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<PersistDB> insertAll(List entitys) throws Exception {
		if ( entitys != null && entitys.size() > 0 ) {
			for (int i = 0; i < entitys.size(); i++)
				insert((PersistDB) entitys.get(i));
		}
		return entitys;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<PersistDB> insertAllLocal(List entitys) throws Exception {
		if ( EntityReflection.haAnnotation(entitys.get(0).getClass(), OnlyLocalStorage.class) ) {
			throw new OffDroidException("N�o foi poss�vel inserir devido a anota��o presente na classe.");
		} else {
			if ( entitys != null && entitys.size() > 0 ) {
				for (int i = 0; i < entitys.size(); i++)
					insertLocal((PersistDB) entitys.get(i));
			}
		}
		return entitys;
	}
	
	public static <T> PersistDB insertLocal(PersistDB entity) throws Exception {
		if ( EntityReflection.haAnnotation(entity.getClass(), OnlyLocalStorage.class) ) {
			throw new OffDroidException("N�o foi poss�vel inserir devido a anota��o presente na classe.");
		} else {
			entity = pm.insert(entity);
			return entity;
		}
	}
	
	public static <T> PersistDB insert(PersistDB entity) throws Exception {
		enviarSincronismo();
		Object obj = null;
		if ( NetWorkUtils.isOnline() && !(EntityReflection.haAnnotation(entity.getClass(), OnlyLocalStorage.class)))
			entity = (PersistDB) service.post(entity);
		entity = (PersistDB) (obj != null ? obj : entity);
		if ( !(EntityReflection.haAnnotation(entity.getClass(), OnlyOnLine.class)) )
			entity = pm.insert(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public static <T> PersistDB update(PersistDB entity) throws Exception {
		enviarSincronismo();
		Object obj = null;
		if ( NetWorkUtils.isOnline() && !(EntityReflection.haAnnotation(entity.getClass(), OnlyLocalStorage.class)))
			obj = (T) service.put(entity);
		entity = (PersistDB) (obj != null ? obj : entity);
		if ( !(EntityReflection.haAnnotation(entity.getClass(), OnlyOnLine.class)) )
			pm.update(entity);
		return entity;
	}
	
	public static void delete(PersistDB entity) throws Exception{
		enviarSincronismo();
		if ( NetWorkUtils.isOnline() && !(EntityReflection.haAnnotation(entity.getClass(), OnlyLocalStorage.class)))
			service.delete(entity, (Integer) EntityReflection.getID(entity));
		pm.remove(entity);
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
	
	public synchronized static void enviarSincronismo() {
		new Thread(new Runnable() { 
            public void run(){        
				List<Sincronizacao> bdLocal;
				try {
					bdLocal = pm.findAll(Sincronizacao.class);
					if ( bdLocal != null && NetWorkUtils.isOnline() ) {
						for (Sincronizacao sync : bdLocal) {
							//Carregar Classe para sincronismo
							Object entity;
							entity = Class.forName(sync.getClasse()).getConstructor().newInstance();
							FieldReflection.setValue(entity, entity.getClass(), FieldReflection.getField(sync.getClass(),"idClasse"), sync.getIdClasse());
							pm.find(entity);
							
							//Setar Id's das Classes para null 
							List<Field> fields = EntityReflection.getEntityFields(entity.getClass());
							for (Field field : fields) {
								if (EntityReflection.isAnnotation(field, ManyToOne.class)) {
									field.setAccessible(true);
									Object value = field.get(entity);
									FieldReflection.setValue(value, value.getClass(), FieldReflection.getField(value.getClass(),"id"), null);
								}
							}
							
							//Realizar Servi�o
							FieldReflection.setValue(entity, entity.getClass(), FieldReflection.getField(entity.getClass(),"id"), null);
							entity = service.post(entity);
							
							//Colocar novo Id das classes
							for (Field field : fields) {
								if (EntityReflection.isAnnotation(field, ManyToOne.class)) {
									field.setAccessible(true);
									Object value = field.get(entity);
									pm.updateIdClass(value, sync.getIdClasse(), (Integer) EntityReflection.getID(entity));
								}
							}
							pm.updateIdClass(entity, sync.getIdClasse(), (Integer) EntityReflection.getID(entity));
							pm.remove(entity);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
		}).start();
	}

	public static Object login(CriteryaSQLite criteria) throws Exception {
		if ( criteria.isLocal() ) {
			return pm.find(new QuerySample(criteria.getClassEntity(), criteria.getWheres(), 
						criteria.getOrderBy(), criteria.getLimit()).toSql(), criteria.getClassEntity());
		} else {
			if ( EntityReflection.haAnnotation(criteria.getClassEntity(), OnlyOnLine.class) ) {
				if ( NetWorkUtils.isOnline() ) {
					return service.login(criteria);
				} else {
					return null;
				}
			} else if ( EntityReflection.haAnnotation(criteria.getClassEntity(), OnlyLocalStorage.class) ) {
				return pm.find(new QuerySample(criteria.getClassEntity(), criteria.getWheres(), criteria.getOrderBy(), criteria.getLimit()).toSql(), criteria.getClassEntity());
			} else {
				if ( NetWorkUtils.isOnline() ) {
					return service.login(criteria);
				} else {
					return pm.find(new QuerySample(criteria.getClassEntity(), criteria.getWheres(), criteria.getOrderBy(), criteria.getLimit()).toSql(), criteria.getClassEntity());
				}
			}
		}
	}
	
}