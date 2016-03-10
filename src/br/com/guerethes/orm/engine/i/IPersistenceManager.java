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
 */
package br.com.guerethes.orm.engine.i;

import java.util.List;

import br.com.guerethes.orm.exception.NotEntityException;
import br.com.guerethes.orm.util.log.i.ILog;

public interface IPersistenceManager {

	public <T> T insert(T entity);

	public <T> void remove(T entity);

	public <T> void removeAll(T[] entities);

	public <T> void update(T entity);

	public <T> T find(T entity) throws Exception;

	public <T> List<T> findAll(Class<T> classe) throws Exception;

	public <T> T find(String sqlSelect, Class<T> targetClass) throws Exception;
	
	public <T> List<T> findAll(String sqlSelect, Class<T> targetClass) throws Exception;

	public String getDataBaseName();

	public int getDataBaseVersion();

	public void executeNativeSql(String sql);

	public void executeManynativeSql(String[] manySql);

	public void loadClasses(List<String> entityClasses) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NotEntityException;

	public ILog logger();
	
	public <T> List<T> findAllEntity(T entity, String... fields);

	public <T> void insertAll(List<T> entityes);
	
	public void updateIdClass(Object entity, int id, int newId);
	
	public void closeBd();
	
}