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

public class Restriction {

	/**
	 * Restrição condicional IGUAIL (<field> = <value>).
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static Eq eq(String field, Object value) {
		return Eq.create(field, value);
	}

	/**
	 * Restrição condicional MAIOR QUE (<field> > <value>)
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static Gt gt(String field, Object value) {
		return Gt.create(field, value);
	}

	/**
	 * Restrição condicional MAIOR OU IGUAL A (<field> >= <value>)
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static Ge gte(String field, Object value) {
		return Ge.create(field, value);
	}

	/**
	 * Restrição condicional IN (<field> IN(<values>0,..,<values>n)).
	 * 
	 * @param field
	 * @param values
	 * @return
	 */
	public static In in(String field, Object[] values) {
		return In.create(field, values);
	}

	/**
	 * Restrição condicional MENOR QUE (<field> < <value>).
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static Lt lt(String field, Object value) {
		return Lt.create(field, value);
	}

	/**
	 * Restrição condicional MENOR OU IGUAL A (<field> <= <value>).
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static Le lte(String field, Object value) {
		return Le.create(field, value);
	}

	/**
	 * Restrição condicional DIFERENTE (<field> <> <value>).
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static Ne ne(String field, Object value) {
		return Ne.create(field, value);
	}

	/**
	 * Restrição condicional NOT IN (not (<field> IN(<values>0,..,<values>n))).
	 * 
	 * @param field
	 * @param values
	 * @return
	 */
	public static Ni ni(String field, Object[] values) {
		return Ni.create(field, values);
	}

	/**
	 * Restrição condicional IS NULL (<field> IS NULL).
	 * 
	 * @param field
	 * @return
	 */
	public static Null isNull(String field) {
		return Null.create(field);
	}

	/**
	 * Restrição condicional LIKE (<field> LIKE (<value>)).
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static Lk lk(String field, String value) {
		return Lk.create(field, value);
	}

	/**
	 * Orderby ASC
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static OrderByAsc orderByAsc(String field) {
		return OrderByAsc.create(field);
	}

	/**
	 * Orderby DESC
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static OrderByDesc orderByDesc(String field) {
		return OrderByDesc.create(field);
	}

	/**
	 * Limit
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static Limit limit(String field) {
		return Limit.create(field);
	}

}