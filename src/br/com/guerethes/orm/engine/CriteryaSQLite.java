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
package br.com.guerethes.orm.engine;

import java.util.ArrayList;
import java.util.List;

import br.com.guerethes.orm.Factory.OffDroidManager;
import br.com.guerethes.orm.engine.criterya.Restriction;
import br.com.guerethes.orm.engine.criterya.pattern.ElementsQueryModel1;

public class CriteryaSQLite {

	private Class<?> classEntity;
	private boolean local;
	private EstrategiaURL estrategia;
	private List<Object> innerEntity;
	private List<ElementsQueryModel1> wheres = new ArrayList<ElementsQueryModel1>();
	private List<ElementsQueryModel1> orderBy = new ArrayList<ElementsQueryModel1>();
	private List<ElementsQueryModel1> limit = new ArrayList<ElementsQueryModel1>();

	@SuppressWarnings("rawtypes")
	private CriteryaSQLite(Class e, boolean local) {
		super();
		this.classEntity = e;
		this.local = local;
	}

	@SuppressWarnings("rawtypes")
	private CriteryaSQLite(Class e, EstrategiaURL estrategia) {
		super();
		this.classEntity = e;
		this.estrategia = estrategia;
	}

	@SuppressWarnings("rawtypes")
	private CriteryaSQLite(Class e, EstrategiaURL estrategia, boolean local) {
		super();
		this.classEntity = e;
		this.estrategia = estrategia;
		this.local = local;
	}
	
	public static CriteryaSQLite from(Class<?> clazz) {
		return new CriteryaSQLite(clazz, false);
	}
	
	public static CriteryaSQLite from(Class<?> clazz, boolean bdLocal, EstrategiaURL estrategiaURL) {
		return new CriteryaSQLite(clazz, estrategiaURL, bdLocal);
	}
	
	public static CriteryaSQLite from(Class<?> clazz, boolean bdLocal) {
		return new CriteryaSQLite(clazz, bdLocal);
	}

	public static CriteryaSQLite from(Class<?> clazz, EstrategiaURL estrategiaURL) {
		return new CriteryaSQLite(clazz, estrategiaURL);
	}
	
	public void add(ElementsQueryModel1 restriction) {
		restriction.setClassEntity(classEntity);
		wheres.add(restriction);
	}

	public void orderby(ElementsQueryModel1 restriction) {
		restriction.setClassEntity(classEntity);
		orderBy.add(0, restriction);
	}
	
	public void limit(ElementsQueryModel1 restriction) {
		restriction.setClassEntity(classEntity);
		limit.add(0, restriction);
	}
	
	public Class<?> getClassEntity() {
		return classEntity;
	}

	public void setClassEntity(Class<?> classEntity) {
		this.classEntity = classEntity;
	}

	public List<Object> getInnerEntity() {
		return innerEntity;
	}

	public void setInnerEntity(List<Object> innerEntity) {
		this.innerEntity = innerEntity;
	}

	public List<ElementsQueryModel1> getWheres() {
		return wheres;
	}

	public void setWheres(List<ElementsQueryModel1> wheres) {
		this.wheres = wheres;
	}

	public List<ElementsQueryModel1> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(List<ElementsQueryModel1> orderBy) {
		this.orderBy = orderBy;
	}

	public List<ElementsQueryModel1> getLimit() {
		return limit;
	}

	public void setLimit(List<ElementsQueryModel1> limit) {
		this.limit = limit;
	}

	public List<?> toList() throws Exception{
		return (List<?>) OffDroidManager.find(this);
	}

	public <T> Object toUniqueResult() throws Exception {
		limit(Restriction.limit("1"));
		List<?> list = (List<?>) OffDroidManager.find(this);
		if ( list != null && !list.isEmpty() )
			return list.get(0);
		return null;
	}

	public <T> Object login() throws Exception {
		limit(Restriction.limit("1"));
		@SuppressWarnings("unchecked")
		T result = (T) OffDroidManager.login(this);
		if ( result != null  )
			return result;
		return null;
	}
	
	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public EstrategiaURL getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(EstrategiaURL estrategia) {
		this.estrategia = estrategia;
	}

	public boolean isPath() {
		if ( estrategia == null ) {
			return true;
		} else {
			return estrategia.isPath();
		}
	}

	public boolean isQuery() {
		if ( estrategia == null ) {
			return true;
		} else {
			return estrategia.isQuery();
		}
	}

}