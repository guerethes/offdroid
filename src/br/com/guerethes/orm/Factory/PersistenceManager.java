/**
 * I thank GOD for the insatiable desire to acquire knowledge that was given to
 * me. The search for knowledge must be one of our main purposes as human beings.
 * I sincerely hope that this simple tool is in any way useful to the community
 * in general.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package br.com.guerethes.orm.Factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.guerethes.orm.annotation.ddl.ManyToOne;
import br.com.guerethes.orm.engine.CriteryaSQLite;
import br.com.guerethes.orm.engine.EstrategiaAtualizacaoBD;
import br.com.guerethes.orm.engine.GenerateModel;
import br.com.guerethes.orm.engine.criterya.QuerySample;
import br.com.guerethes.orm.engine.criterya.Restriction;
import br.com.guerethes.orm.engine.i.IPersistenceManager;
import br.com.guerethes.orm.engine.i.PersistDB;
import br.com.guerethes.orm.enumeration.ModelBeavior;
import br.com.guerethes.orm.exception.NotEntityException;
import br.com.guerethes.orm.model.Sincronizacao;
import br.com.guerethes.orm.reflection.EntityReflection;
import br.com.guerethes.orm.reflection.FieldReflection;
import br.com.guerethes.orm.util.FieldValue;
import br.com.guerethes.orm.util.content.ContentValueUtil;
import br.com.guerethes.orm.util.content.CursorUtil;
import br.com.guerethes.orm.util.log.i.ILog;
import br.com.guerethes.orm.util.log.i.Log;
import br.com.guerethes.orm.utils.FilterUtils;
import br.com.guerethes.synchronization.annotation.OnlyLocalStorage;
import br.com.guerethes.synchronization.annotation.OnlyOnLine;
import br.com.guerethes.synchronization.networkUtils.NetWorkUtils;

public class PersistenceManager extends SQLiteOpenHelper implements IPersistenceManager {

	private static GenerateModel GEN_MODEL;

	private static String DATABASE_NAME;
	private static int DATABASE_VERSION;

	private SQLiteDatabase sqLiteDatabase;
	private static List<String> classes;
	private EstrategiaAtualizacaoBD estrategia;

	private Log log;

	public SQLiteDatabase getDbSQLite() {
		if (this.sqLiteDatabase == null) {
			this.sqLiteDatabase = getReadableDatabase();
		}
		return this.sqLiteDatabase;
	}

	/**
	 * 
	 * @param context
	 * @param dbName
	 * @param dbVersion
	 * @param mappedClass
	 * @param mode
	 * @param modelBeavior
	 * @param typeLog
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NotEntityException
	 */
	@SuppressLint("UseValueOf")
	public PersistenceManager(Context context, int dbName, int dbVersion, int mappedClass, 
			int mode, ModelBeavior modelBeavior, int typeLog, EstrategiaAtualizacaoBD estrategia)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, NotEntityException {
		super(context, context.getString(dbName).trim(), null, new Integer(context.getString(dbVersion)));
		log = new Log(typeLog);
		this.estrategia = estrategia;
		if ((GEN_MODEL == null) || modelBeavior.equals(ModelBeavior.RENEW)) {
			GEN_MODEL = new GenerateModel();
			GEN_MODEL.loadClasses(context.getString(mappedClass).trim().split(";"));
		}
	}

	/**
	 * 
	 * @param context
	 * @param dataBaseName
	 * @param dataBaseVersion
	 * @param mappedClass
	 * @param mode
	 * @param modelBeavior
	 * @param typeLog
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NotEntityException
	 */
	public PersistenceManager(Context context, String dataBaseName,
			int dataBaseVersion, List<String> mappedClass, int mode,
			ModelBeavior modelBeavior, int typeLog, EstrategiaAtualizacaoBD estrategia)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, NotEntityException {
		super(context, dataBaseName.trim(), null, dataBaseVersion);
		log = new Log(typeLog);
		this.estrategia = estrategia;
		if ((GEN_MODEL == null) || modelBeavior.equals(ModelBeavior.RENEW)) {
			GEN_MODEL = new GenerateModel();
			GEN_MODEL.loadClasses(mappedClass);
		}
		DATABASE_NAME = dataBaseName;
		DATABASE_VERSION = dataBaseVersion;
		classes = mappedClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.IPersistenceManager#loadClasses(
	 * java.util.List)
	 */
	@Override
	public void loadClasses(List<String> entityClasses)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, NotEntityException {
		GEN_MODEL.loadClasses(entityClasses);
	}

	@Override
	public synchronized <T> T insert(T entity) {
		return insert(entity, true);
	}
	
	private synchronized <T> T insert(T entity, boolean classePrincipal) {
		if ( !(EntityReflection.haAnnotation(entity.getClass(), OnlyOnLine.class)) ) {
			Class<?> entityClass = entity.getClass();
			List<Field> fields = EntityReflection.getEntityFields(entityClass);
			for (Field field : fields) {
				if ( EntityReflection.isAnnotation(field, ManyToOne.class) ) {
					try {
						field.setAccessible(true);
						PersistDB value = (PersistDB) field.get(entity);
						if( value != null )
							insert(value, false);
						else {
							PersistDB obj = (PersistDB) field.getType().newInstance();  
							obj.setId(-1);
							FieldReflection.setValue(entity, entity.getClass(), field, obj);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			//Essa parte vai inserir a classe que est� dentro do la�o.
			try {
				insertAux((PersistDB) entity, classePrincipal);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return entity;
	}

	private synchronized void insertAux(PersistDB entity, boolean classePrincipal) throws Exception {
		try {
			getDbSQLite().beginTransaction();
			PersistDB obj = (PersistDB) entity.getClass().newInstance();
			if ( EntityReflection.getID(entity) != null ) {
				//Auxiliar
				String tableName = EntityReflection.getTableName(entity.getClass());
				HashMap<String, FieldValue> fieldValues = EntityReflection.getFieldValues(entity);
				
				obj.setId((Integer) EntityReflection.getID(entity));
				CriteryaSQLite criteria = CriteryaSQLite.from(entity.getClass(), true);
				criteria.add(Restriction.eq("id", obj.getId()));
				obj = (PersistDB) criteria.toUniqueResult();
				if ( obj != null ) {
					if ( !obj.equals(entity) )
						update(entity);
				} else {
					create(entity, classePrincipal, tableName, fieldValues);
				}
			} else {
				//Auxiliar
				String tableName = EntityReflection.getTableName(entity.getClass());
				entity.setId(nextVal(entity));
				HashMap<String, FieldValue> fieldValues = EntityReflection.getFieldValues(entity);
				create(entity, classePrincipal, tableName, fieldValues);
			}
		} catch (SQLException e) {
			log.e("[OffDroid]-<SQLException>", e.toString());
			throw e;
		} finally {
			if (getDbSQLite().inTransaction()) {
				getDbSQLite().endTransaction();
			}
		}
	}

	private synchronized void create(PersistDB entity, boolean classePrincipal, String tableName, HashMap<String, FieldValue> fieldValues) {
		ContentValues cv = ContentValueUtil.putAll(fieldValues);
		getDbSQLite().insert(tableName, null, cv);
		if (classePrincipal)	
			insertSync(entity);
		getDbSQLite().setTransactionSuccessful();
		log.i("OffDroid", "INSERT [" + entity.getClass().getSimpleName() + "] - [ ID: "+ EntityReflection.getID(entity)+"].");
	}

	private synchronized int nextVal(Object entity){
		String query = "SELECT max("+FieldReflection.getColumnName(entity.getClass(), "id")+") as seq FROM '" + EntityReflection.getTableName(entity.getClass())+ "'" +
				" WHERE " + FieldReflection.getColumnName(entity.getClass(), "id") + " < 0 limit 1";
	    SQLiteDatabase db = getDbSQLite();
	    Cursor cursor = db.rawQuery(query, null);
	    String lastId = null;
	    if (cursor.moveToLast()) {
	        do{
	        	lastId = cursor.getString(cursor.getColumnIndex("seq"));
	        }while (cursor.moveToNext());
	    }
	    cursor.close();
	    
		//Verificando se o id o objeto e maior que zero, ou seja, esse obj foi importado do servidor.
		boolean local = EntityReflection.haAnnotation(entity.getClass(), OnlyLocalStorage.class);
		
		int newId = 0;
		if ( !local ) {
			if (lastId == null ) {
				newId = Integer.MIN_VALUE;
			} else {
				newId = Integer.parseInt(lastId) + 1;
			}
		}
				
		return newId;
	}

	public synchronized void updateIdClass(Object entity, int id, int newId) {
		executeNativeSql("UPDATE "+ EntityReflection.getTableName(entity.getClass()) + 
	    		" SET " + FieldReflection.getColumnName(entity.getClass(), "id") +" = " + newId + 
	    		" WHERE " + FieldReflection.getColumnName(entity.getClass(), "id") +" = " + id);
		FieldReflection.setValue(entity, entity.getClass(), FieldReflection.getField(entity.getClass(), "id"), newId);
	}
	
	private synchronized void insertSync(Object entity){
		if ( !NetWorkUtils.isOnline() && !(EntityReflection.haAnnotation(entity.getClass(), OnlyOnLine.class)) ) {
			Sincronizacao sincronizacao = new Sincronizacao(entity.getClass().getName(), (Integer) EntityReflection.getID(entity));
			String tableName = EntityReflection.getTableName(sincronizacao.getClass());
			HashMap<String, FieldValue> fieldValues = EntityReflection.getFieldValues(sincronizacao);
			ContentValues cv = ContentValueUtil.putAll(fieldValues);
			getDbSQLite().insert(tableName, null, cv);
		}
	}
	
	public synchronized <T> void insertAll(List<T> entityes) {
		for (T t : entityes) {
			insert(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.IPersistenceManager#remove(java.
	 * lang.Object)
	 */
	@Override
	public synchronized <T> void remove(T entity) {
		Class<?> entityClass = entity.getClass();
		String tableName = EntityReflection.getTableName(entityClass);
		String[] whereArgs = new String[] { EntityReflection.getID(entity).toString() };
		getDbSQLite().delete(tableName, "_ID=?", whereArgs);
		log.i("OffDroid", "Object id[" + entityClass.getSimpleName() + "<" + whereArgs[0] + ">]-REMOVE.");
	}

	@Override
	public synchronized <T> void removeAll(T[] entities) {

		Class<?> entityClass = entities[0].getClass();
		String tableName = EntityReflection.getTableName(entityClass);
		List<String> ids = new ArrayList<String>();
		for (Object e : entities) {
			ids.add(EntityReflection.getID(e).toString());
		}

		for (String where : ids) {
			getDbSQLite().delete(tableName, "_ID=?", (new String[] { where }));
		}
		log.i("OffDroid", "Object id[" + entityClass.getSimpleName() + ">]-REMOVE_ALL.");
	}

	@Override
	public synchronized <T> void update(T entity) {
		if ( !(EntityReflection.haAnnotation(entity.getClass(), OnlyOnLine.class)) ) {
			Class<?> entityClass = entity.getClass();
			String tableName = EntityReflection.getTableName(entityClass);
			HashMap<String, FieldValue> fieldValues;
			fieldValues = EntityReflection.getFieldValues(entity);
			
			List<Field> fields = EntityReflection.getEntityFields(entityClass);
			for (Field field : fields) {
				if (EntityReflection.isAnnotation(field, ManyToOne.class)) {
					try {
						field.setAccessible(true);
						Object value = field.get(entity);
						if (value != null)
							update(value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			ContentValues cv = ContentValueUtil.putAll(fieldValues);
			getDbSQLite().update(tableName, cv, "_ID=?", (new String[] { cv.get("_ID").toString() }));
			log.i("OffDroid", "Object-[" + entityClass.getSimpleName()+ "]-UPDATE.");	
		}
		
	}

	public synchronized <T> void updateAll(List<T> entities) {
		for (T entity : entities) {
			update(entity);
		}
	}

	@Override
	public <T> T find(T entity) throws IllegalAccessException, IllegalArgumentException {
		return findEntity(entity, true);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findAllEntity(T entity, String... fields) {
		List<T> all = ((List<T>) findAll((new QuerySample(entity)).toSql(), entity.getClass()));
		if ( all != null ) {
			Object[] values = new Object[fields.length];
			for (int i = 0; i < values.length; i++)
				values[i] = FieldReflection.getValue(entity, fields[i]);
			return (List<T>) FilterUtils.filterBy(all, fields, values);
		} else {
			return null;	
		}
	}

	public <T> T findEntity(T entity, boolean exibir) throws IllegalAccessException, IllegalArgumentException {
		if ( !(EntityReflection.haAnnotation(entity.getClass(), OnlyOnLine.class)) ) {
			SQLiteDatabase db = getDbSQLite();
			Cursor c = db.rawQuery((new QuerySample(entity)).toSql(), null);
	
			if (c.getCount() == 1) {
				c.moveToFirst();
				CursorUtil.loadFieldsInCursor(c, entity);
				
				List<Field> campos = EntityReflection.getEntityFields(entity.getClass());
				for (Field field : campos) {
					if (EntityReflection.isAnnotation(field, ManyToOne.class)) {
						field.setAccessible(true);
						findEntity(EntityReflection.getField(entity, field.getName()), false);
					}
				}
				
				c.close();
				if (exibir) {
					log.i("OffDroid", "Object-[" + entity.getClass().getSimpleName() + "]-FIND.");	
				}
				return entity;
			} else {
				c.close();
				return null;
			}
		}
		return null;
	}

	@Override
	public <T> List<T> findAll(Class<T> classe) throws InstantiationException,IllegalAccessException {
		return (List<T>) findAll((new QuerySample(classe.newInstance())).toSql(), classe);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(String sqlSelect, Class<T> classEntity) {

		SQLiteDatabase db = getDbSQLite();
		Cursor c = db.rawQuery(sqlSelect, null);
		List<T> lEntity = null;
		if (c.moveToFirst()) {
			lEntity = new ArrayList<T>();
			do {
				try {
					Object e = classEntity.newInstance();
					lEntity.add((T) e);
					CursorUtil.loadFieldsInCursor(c, e);
					List<Field> fields = EntityReflection.getEntityFields(classEntity);
					for (Field field : fields) {
						if (EntityReflection.isAnnotation(field,ManyToOne.class)) {
							field.setAccessible(true);
							findEntity(field.get(e), false);
						}
					}
				} catch (InstantiationException e1) {
					log.e("Erro", e1.getMessage());
				} catch (IllegalAccessException e1) {
					log.e("Erro", e1.getMessage());
				}
			} while (c.moveToNext());
			c.close();
		} else {
			c.close();
		}
		return lEntity;

	}

	@Override
	public <T> T find(String sqlSelect, Class<T> classEntity) {
		List<T> list = findAll(sqlSelect, classEntity);
		if ( list != null )
			return list.get(0);
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.db.orm.engine.IPersistenceManager#getDataBaseName()
	 */
	@Override
	public String getDataBaseName() {
		return DATABASE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.db.orm.engine.IPersistenceManager#getDataBaseVersion()
	 */
	@Override
	public int getDataBaseVersion() {
		return DATABASE_VERSION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		try {
			this.sqLiteDatabase = db;
			this.sqLiteDatabase.beginTransaction();
			this.executeManynativeSql(GEN_MODEL.getSQLModel());
			this.sqLiteDatabase.setTransactionSuccessful();
		} catch (SQLException e) {
			log.e("[STDERR]-<SQLException>", e.toString());
			throw e;
		} finally {
			if (this.sqLiteDatabase.inTransaction()) {
				this.sqLiteDatabase.endTransaction();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			this.sqLiteDatabase = db;
			this.sqLiteDatabase.beginTransaction();
			if ( estrategia.isCreate() ) {
				this.executeManynativeSql(GEN_MODEL.getSQLDropModel());
				this.sqLiteDatabase.setTransactionSuccessful();
				onCreate(db);
			} else if ( estrategia.isUpdate() ) {
				for (String clazz : classes) {
					Class<?> c = Class.forName(clazz.trim());
					String table = EntityReflection.getTableName(c);
					Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM " + table, null);
					if (cursor != null) {
						List<Field> fields = EntityReflection.getEntityFields(c);
						for (Field field : fields) {
							String column = FieldReflection.getColumnName(c, field);
							if (cursor.moveToFirst()) {
								int columnIndex = cursor.getColumnIndex(column);
								int columnType = cursor.getType(columnIndex);
								if (columnIndex < 0)
									executeNativeSql("ALTER TABLE " + table + " ADD COLUMN " + FieldReflection.getColumnNameDDL(c, field) + ";");
								else if ( columnIndex > 0 && columnType == 0 ) {
									executeNativeSql("ALTER TABLE " +table+ " RENAME TO "+table+"_old");
									executeManynativeSql(GEN_MODEL.getSQLModel(table));
								} 
							}
						}
					}
				}
			} 
			this.sqLiteDatabase.setTransactionSuccessful();
		} catch (Exception e) {
			log.e("[STDERR]-<SQLException>", e.toString());
		} finally {
			if (this.sqLiteDatabase.inTransaction()) {
				this.sqLiteDatabase.endTransaction();
			}
		}
	}

	public void executeNativeSql(String sql) {
		if (sql.trim().length() > 0) {
			getDbSQLite().execSQL(sql);
		}
	}

	public void executeManynativeSql(String[] manySql) {
		if (manySql.length > 0) {
			for (String sql : manySql) {
				this.executeNativeSql(sql);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.softctrl.h4android.orm.engine.i.IPersistenceManager#logger()
	 */
	@Override
	public ILog logger() {
		return log;
	}

	@Override
	public void closeBd() {
		getDbSQLite().close();
	}

}