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

import java.util.List;
import java.util.Map;

import br.com.guerethes.orm.engine.criterya.pattern.ElementsQueryModel1;
import br.com.guerethes.orm.engine.criterya.pattern.IElementsQuery;
import br.com.guerethes.orm.reflection.FieldReflection;

public class OrderByAsc extends ElementsQueryModel1 {

	private OrderByAsc(String nameObject) {
		super(nameObject);
	}

	public static OrderByAsc create(String field) {
		OrderByAsc asc = new OrderByAsc(field);
		return asc;
	}

	@Override
	public String toSql() {
		String sColumn = "";
		if ( getName().lastIndexOf(".") != -1 ) {
			List<Object> values = Query.getJoin().get(getName().substring(0, getName().lastIndexOf(".")));
			sColumn = values.get(0) + "." + FieldReflection.getColumnName( (Class<?>) values.get(1), getName().substring(getName().lastIndexOf(".")+1, getName().length()));
		} else {
			sColumn = FieldReflection.getColumnName(getClassEntity(), getName());
		}
		return String.format(" ORDER BY %s ASC", sColumn);
	}
	
	@Override
	public IElementsQuery add(IElementsQuery iElementsQuery) {
		return null;
	}

	@Override
	public IElementsQuery addOrder(IElementsQuery iElementsQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IElementsQuery addLimit(IElementsQuery iElementsQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> toSqlMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
