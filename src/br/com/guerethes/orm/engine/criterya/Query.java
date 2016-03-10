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
package br.com.guerethes.orm.engine.criterya;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.util.Log;
import br.com.guerethes.orm.annotation.ddl.ManyToOne;
import br.com.guerethes.orm.engine.criterya.pattern.ElementsQuery;
import br.com.guerethes.orm.engine.criterya.pattern.IElementsQuery;
import br.com.guerethes.orm.engine.i.IPersistenceManager;
import br.com.guerethes.orm.exception.SelectAllQueryException;
import br.com.guerethes.orm.reflection.EntityReflection;
import br.com.guerethes.orm.reflection.FieldReflection;

public class Query extends ElementsQuery {

	private enum TypeSelect {
		ALL, OTHER
	}

	private IPersistenceManager pm;

	@SuppressWarnings("unused")
	private String select = "*";
	private TypeSelect typeQuery = TypeSelect.ALL;
	private HashMap<Integer, IElementsQuery> where;
	private HashMap<Integer, IElementsQuery> order;
	private HashMap<Integer, IElementsQuery> limit;
	private int count = 0;

	private static HashMap<String, List<Object>> join = null;

	public static HashMap<String, List<Object>> getJoin() {
		if ( join == null )
			join = new HashMap<String, List<Object>>();
		return join;
	}
	
	protected Query(String nameObject) {
		super(nameObject);
	}

	public static <T> Query create(Class<T> classEntity, IPersistenceManager pm) {
		String entityName = EntityReflection.getTableName(classEntity);
		Query q = new Query(entityName);
		q.pm = pm;
		q.setClassEntity(classEntity);
		return q;
	}

	@Override
	public IElementsQuery add(IElementsQuery iElementsQuery) {
		if ( where == null )
			where = new HashMap<Integer, IElementsQuery>();	
		where.put(++count, iElementsQuery);
		return this;
	}

	@Override
	public IElementsQuery addOrder(IElementsQuery iElementsQuery) {
		if ( order == null )
			order = new HashMap<Integer, IElementsQuery>();
		order.put(++count, iElementsQuery);
		return this;
	}

	@Override
	public IElementsQuery addLimit(IElementsQuery iElementsQuery) {
		if ( limit == null )
			limit = new HashMap<Integer, IElementsQuery>();
		limit.put(++count, iElementsQuery);
		return this;
	}
	
	@Override
	public String toSql() {
		String sql = "SELECT t.* FROM %s as t %s WHERE (1=1) %s %s;";
		join = null;
		count = 0;
		String joinClausules  = joins(getClassEntity(), "", "t");
		String whereClausules = "";
		String orderClausules = "";
		
		if ( where != null ) {
			for (Entry<Integer, IElementsQuery> e : where.entrySet())
				whereClausules += e.getValue().toSql();
		}
		
		if ( order != null ) {
			for (Entry<Integer, IElementsQuery> e : order.entrySet())
				orderClausules += e.getValue().toSql();
		}

		if ( limit != null ) {
			for (Entry<Integer, IElementsQuery> e : limit.entrySet())
				orderClausules += e.getValue().toSql();
		}
		
		Log.d("OffDroidQuery", String.format(sql, getKey(), joinClausules, whereClausules, orderClausules));
		return String.format(sql, getKey(), joinClausules, whereClausules, orderClausules);
	}

	private String joins(Class<?> classe, String attr, String currentJoin) {
		String join = "";
		List<Field> fields = EntityReflection.getEntityFields(classe);
		for (Field field : fields) {
			if (EntityReflection.isAnnotation(field, ManyToOne.class)) {
				field.setAccessible(true);
				String nameJoin = "join_" + (count++) + "_";
				
				if ( attr == "" )
					attr += field.getName();
				else
					attr += "." + field.getName();
				
				//Info Join
				List<Object> dataTemp = new ArrayList<Object>();
				dataTemp.add(nameJoin);
				dataTemp.add(field.getType());
				getJoin().put(attr, dataTemp);
				
				Class<?> classTemp = field.getType();
				join += "LEFT JOIN " + EntityReflection.getTableName(classTemp) + " as " + nameJoin + 
					" ON ( " + currentJoin + "." + FieldReflection.getColumnName(classe, field) + " = " + 
					nameJoin + "." + FieldReflection.getColumnName(classTemp, "id") + " ) ";
				join += joins(classTemp, attr, nameJoin);
			}
		}
		return join;
	}

	public Query selectCount() {
		select = "COUNT(*)";
		typeQuery = TypeSelect.OTHER;
		return this;
	}

	public Query selectMax(String field) {
		select = "MAX(" + FieldReflection.getColumnName(getClassEntity(), field) + ")";
		typeQuery = TypeSelect.OTHER;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> java.util.List<T> list() throws Exception {
		if (typeQuery != TypeSelect.ALL)
			new SelectAllQueryException("Tipo de consulta n�o retorna list.");
		String sql = toSql();
		pm.logger().i("List", "Query-[" + sql + "]-Executed.");
		return (List<T>) pm.findAll(sql, getClassEntity());
	}

	@Override
	public String getKey() {
		return EntityReflection.getTableName(getClassEntity());
	}

	@Override
	public Object getValue() {
		return null;
	}

}