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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import br.com.guerethes.orm.annotation.ddl.Enumerated;
import br.com.guerethes.orm.annotation.ddl.ManyToOne;
import br.com.guerethes.orm.engine.criterya.pattern.ElementsQueryModel1;
import br.com.guerethes.orm.reflection.EntityReflection;
import br.com.guerethes.orm.reflection.FieldReflection;
import br.com.guerethes.orm.util.EnumerateUtils;
import br.com.guerethes.orm.util.FieldValue;

public class QuerySample extends Query {

	private QuerySample(String nameObject) {
		super(nameObject);
	}

	public QuerySample(Object entity) {
		super(EntityReflection.getTableName(entity.getClass()));
		Class<?> classEntity = entity.getClass();
		setClassEntity(classEntity);
		HashMap<String, FieldValue> fieldValues;
		fieldValues = EntityReflection.getFieldValues(entity);
		for (Entry<String, FieldValue> fv : fieldValues.entrySet()) {
			//Verifica se Ž classe mapeada
			if ( !(fv.getValue().getField().isAnnotationPresent(ManyToOne.class))) {
				Object value = null;
				value = FieldReflection.getValue(entity, classEntity, fv.getValue().getField().getName());
				if (value != null) {
					if (fv.getValue().getField().getType().isEnum()) {
						Enumerated en = fv.getValue().getField().getAnnotation(Enumerated.class);
						value = EnumerateUtils.getValueInEnum(value, en.value());
					}
					Eq eq = Eq.create(fv.getValue().getField().getName(), value);
					eq.setClassEntity(classEntity);
					add(eq);
				}
			}
		}
	}

	public QuerySample(Class<?> classEntity, Collection<ElementsQueryModel1> where, Collection<ElementsQueryModel1> orderby, Collection<ElementsQueryModel1> limit) {
		super(EntityReflection.getTableName(classEntity));
		setClassEntity(classEntity);
		for (ElementsQueryModel1 condiction : where)
			add(condiction);
		for (ElementsQueryModel1 order : orderby)
			addOrder(order);
		for (ElementsQueryModel1 li : limit)
			addLimit(li);
	}
	
}