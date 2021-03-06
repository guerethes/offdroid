/**
 * 
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
package br.com.guerethes.orm.beavior.column;

import java.lang.reflect.Field;

import br.com.guerethes.orm.annotation.ddl.Enumerated;
import br.com.guerethes.orm.annotation.validation.NotNull;
import br.com.guerethes.orm.beavior.column.i.IColumnBeavior;
import br.com.guerethes.orm.enumeration.validation.TypeEnum;

public class ColumnBeaviorEnumerated implements IColumnBeavior {

	@Override
	public String getNameColumn(Field field) {
		return "_" + field.getName().toUpperCase() + "_ENUM";
	}

	@Override
	public String getDDLColumn(Field field) {

		String ddl;
		Enumerated enumerated = field.getAnnotation(Enumerated.class);
		ddl = "_" + field.getName().toUpperCase() + "_ENUM ";
		ddl += (enumerated.value().equals(TypeEnum.STRING) ? "VARCHAR(255)"
				: "INTEGER");
		ddl += (field.isAnnotationPresent(NotNull.class) ? " NOT NULL" : "");
		return ddl;

	}

}
