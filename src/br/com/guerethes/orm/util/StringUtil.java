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
package br.com.guerethes.orm.util;

public final class StringUtil {

	private static final String SQL_CREATE_TABLE = "CREATE TABLE %s (%s);";
	private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS %s;";

	public static java.lang.String capitalize(java.lang.String value) {
		return value.substring(0, 1).toUpperCase()
				+ value.substring(1, value.length());
	}

	public static java.lang.String obterColumns(
			java.util.List<java.lang.String> columns) {
		String result = columns.toString().substring(1);
		return result.substring(0, result.length() - 1);
	}

	public static java.lang.String createSQL(java.lang.String tableName,
			java.util.List<java.lang.String> columns) {
		return String
				.format(SQL_CREATE_TABLE, tableName, obterColumns(columns));
	}

	public static java.lang.String dropSQL(java.lang.String tableName) {
		return String.format(SQL_DROP_TABLE, tableName);
	}

	public static String objectToString(Object o) {
		Class<?> c = o.getClass();
		if (c.equals(String.class)) {
			return "'" + o.toString() + "'";
		}
		return o.toString();
	}

}
