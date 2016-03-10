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

import br.com.guerethes.orm.engine.criterya.pattern.ElementsQueryModel1;
import br.com.guerethes.orm.engine.criterya.pattern.IElementsQuery;
import br.com.guerethes.orm.exception.MethodDeprecatedInException;

/**
 * "Maior que"
 * 
 * @author <a href="mailto:carlostimoshenkorodrigueslopes@gmail.com">Timoshenko</a>.
 * @version $Revision: 0.0.0.1 $
 */
public class Gt extends ElementsQueryModel1 {

	private Gt(String nameObject) {
		super(nameObject);
	}

	public static Gt create(String field, Object value) {

		Gt gt = new Gt(field);
		gt.value = value;
		gt.SIGNAL_OPERATOR = ">";
		return gt;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.softctrl.h4android.orm.engine.criterya.pattern.IElementsQuery#
	 * add(br.com.softctrl.h4android.orm.engine.criterya.pattern.IElementsQuery)
	 */
	@Deprecated
	@Override
	public IElementsQuery add(IElementsQuery iElementsQuery) {
		new MethodDeprecatedInException(
				"method <add> not suprted in Gt restriction.");		
		return null;
	}

	@Override
	public <T> List<T> list() {
		new MethodDeprecatedInException(
				"method <list> not suprted in Gt restriction.");
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

}
