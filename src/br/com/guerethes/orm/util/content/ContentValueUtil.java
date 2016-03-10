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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;

import android.content.ContentValues;
import br.com.guerethes.orm.annotation.ddl.Enumerated;
import br.com.guerethes.orm.annotation.ddl.ManyToOne;
import br.com.guerethes.orm.enumeration.validation.TypeEnum;
import br.com.guerethes.orm.reflection.EntityReflection;
import br.com.guerethes.orm.util.EnumerateUtils;
import br.com.guerethes.orm.util.FieldValue;

/**
 * @author <a href="mailto:carlostimoshenkorodrigueslopes@gmail.com">Timoshenko</a>.
 * @version $Revision: 0.0.0.1 $
 * 
 */
public class ContentValueUtil {

	/**
	 * Transforma um hasmap em um contentvalue
	 * 
	 * @param values
	 */
	public static ContentValues putAll(HashMap<String, FieldValue> values) {

		ContentValues cvs = new ContentValues();
		for (Entry<String, FieldValue> value : values.entrySet()) {
			put(cvs, value.getKey(), value.getValue());
		}
		return cvs;
	}

	private static Class<?> getClassEnum(TypeEnum typeEnum) {

		if (typeEnum == TypeEnum.STRING) {
			return String.class;
		} else {
			return Integer.class;
		}

	}

	private static void put(ContentValues cvs, String key, FieldValue fieldValue) {
		Method mPutt;
		try {
			Boolean isNull = (fieldValue.getValue() == null);
			if ( EntityReflection.isAnnotation(fieldValue.getField(), ManyToOne.class) ) {
				mPutt = cvs.getClass().getMethod("put", String.class, Integer.class);
				if ( fieldValue.getValue() != null )
					mPutt.invoke(cvs, key, EntityReflection.getID(fieldValue.getValue()));
				else
					mPutt.invoke(cvs, key, null);
			} else if (isNull) {
				cvs.putNull(key);
			} else {
				Boolean isEnum = fieldValue.getValue().getClass().isEnum();
				Class<?> classPutValue = null;
				Object valuePut = fieldValue.getValue();
				if (isEnum) {
					Enumerated enumeration = fieldValue.getField().getAnnotation(Enumerated.class);
					classPutValue = getClassEnum(enumeration.value());
					mPutt = cvs.getClass().getMethod("put", String.class,classPutValue);
					valuePut = EnumerateUtils.getValueInEnum(fieldValue.getValue(), enumeration.value());
				} else {
					mPutt = cvs.getClass().getMethod("put", String.class, fieldValue.getValue().getClass());
				}
				mPutt.invoke(cvs, key, valuePut);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
