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
package br.com.guerethes.orm.engine.criterya.pattern;

import java.util.List;

import br.com.guerethes.orm.engine.criterya.Query;
import br.com.guerethes.orm.reflection.FieldReflection;
import br.com.guerethes.orm.util.StringUtil;

/**
 * @author <a
 *         href="mailto:carlostimoshenkorodrigueslopes@gmail.com">Timoshenko</
 *         a>.
 * @version $Revision: 0.0.0.1 $
 */
public abstract class ElementsQueryModel1 extends ElementsQuery {

	protected String SIGNAL_OPERATOR = "=";
	protected Object value;

	public ElementsQueryModel1(String nameObject) {
		super(nameObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.criterya.pattern.IElementsQuery#
	 * getValue()
	 */
	@Override
	public Object getValue() {
		return StringUtil.objectToString(value);
	}
	
	public String getValueString() {
		return value.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if ( getName().lastIndexOf(".") != -1 ) {
			List<Object> values = Query.getJoin().get(getName().substring(0, getName().lastIndexOf(".")));
			String sColumn = values.get(0) + "." + FieldReflection.getColumnName( (Class<?>) values.get(1), getName().substring(getName().lastIndexOf(".")+1, getName().length()));
			return String.format("%s %s %s", sColumn, SIGNAL_OPERATOR, getValue());
		} else {
			String sColumn = "t." + FieldReflection.getColumnName(getClassEntity(), getName());
			return String.format("%s %s %s", sColumn, SIGNAL_OPERATOR, getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.criterya.pattern.IElementsQuery#
	 * getKey()
	 */
	@Override
	public String getKey() {
		return FieldReflection.getColumnName(getClassEntity(), getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.criterya.pattern.IElementsQuery#
	 * toSql()
	 */
	@Override
	public String toSql() {
		return String.format(" AND (%s)", toString());	
	}

}