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
package br.com.guerethes.orm.util.content;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import br.com.guerethes.orm.annotation.ddl.Enumerated;
import br.com.guerethes.orm.annotation.ddl.ManyToOne;
import br.com.guerethes.orm.enumeration.validation.TypeEnum;
import br.com.guerethes.orm.reflection.EntityReflection;
import br.com.guerethes.orm.reflection.FieldReflection;

public class CursorUtil extends Activity {

	public static void loadFieldsInCursor(Cursor cursor, Object entity, String alias) {
		Class<?> classEntity = entity.getClass();
		List<Field> fields = EntityReflection.getEntityFields(classEntity);
		for (Field field : fields) {
			if ( FieldReflection.hasColumnBd(field) && !EntityReflection.isAnnotation(field, ManyToOne.class) ) {
				FieldReflection.setValue(entity, classEntity, field, getValue(cursor, classEntity, field, entity, alias));
			}
		}
	}
		
	public static void loadFieldsInCursor(Cursor cursor, Object entity) {
		Class<?> classEntity = entity.getClass();
		List<Field> fields = EntityReflection.getEntityFields(classEntity);
		for (Field field : fields) {
			if ( FieldReflection.hasColumnBd(field) && !EntityReflection.isAnnotation(field, ManyToOne.class) ) {
				FieldReflection.setValue(entity, classEntity, field, getValue(cursor, classEntity, field, entity, null));
			}
		}
	}

	private static Object getValue(Cursor cursor, Class<?> classEntity, Field field, Object entity, String alias) {
		try {
			if ( EntityReflection.isAnnotation(field, ManyToOne.class) ) {
				field.setAccessible(true);
				Object value = null;
				if ( field.get(entity) == null ) {
					value = field.getType().newInstance();
					String columnNameTemp = FieldReflection.getColumnName(field.getDeclaringClass(), field);
					String columnName= "";
					
					if ( alias != null ) {
						columnName = alias + "_" + columnNameTemp;
					} else {
						columnName = columnNameTemp;
					}

					Method m = getMethod(cursor, "getInt");
					FieldReflection.setValue(value, value.getClass(), 
							FieldReflection.getField(value.getClass(), "id"), 
								(Integer) m.invoke(cursor, cursor.getColumnIndex(columnName)));
					return value;
				} else
					return null;	
			} else {
				String columnNameTemp = FieldReflection.getColumnName(classEntity, field.getName());
				String columnName= "";
				
				if ( alias != null ) {
					columnName = alias + "_" + columnNameTemp;
				} else {
					columnName = columnNameTemp;
				}
				
				Method m = getMethod(cursor, getMethodName(field));	
				return m.invoke(cursor, cursor.getColumnIndex(columnName));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Method getMethod(Cursor cursor, String namemethod) {

		try {
			return cursor.getClass().getMethod(namemethod, int.class);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;

	}

	private static String getMethodName(Field field) {

		Class<?> classField = field.getType();
		if (classField.isEnum()) {
			Enumerated enumeration = field.getAnnotation(Enumerated.class);
			if (enumeration.value() == TypeEnum.ORDINAL) {
				return "getInt";
			} else {
				return "getString";
			}
		} else {
			if (classField == Integer.class)
				return "getInt";
			return "get" + classField.getSimpleName();
		}

	}

}
