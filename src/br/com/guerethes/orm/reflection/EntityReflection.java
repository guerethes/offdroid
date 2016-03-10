package br.com.guerethes.orm.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import br.com.guerethes.orm.annotation.ddl.Entity;
import br.com.guerethes.orm.annotation.ddl.Path;
import br.com.guerethes.orm.annotation.ddl.Table;
import br.com.guerethes.orm.util.FieldValue;

public final class EntityReflection {

	public static boolean isEntity(Class<?> targetClass) {
		return targetClass.isAnnotationPresent(Entity.class);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean haAnnotation(Class<?> targetClass, Class annotation) {
		return targetClass.isAnnotationPresent(annotation);
	}
	
	public static String getTableName(Class<?> targetClass) {
		return (targetClass.isAnnotationPresent(Table.class) ? targetClass
			.getAnnotation(Table.class).value() : targetClass
			.getSimpleName().toUpperCase());
	}

	public static String getPathName(Class<?> targetClass) {
		return (targetClass.isAnnotationPresent(Path.class) ? targetClass
			.getAnnotation(Path.class).value() : targetClass
			.getSimpleName().toLowerCase());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean isAnnotation(Field campo, Class annotation) {
		return campo.isAnnotationPresent(annotation);
	}
	
	public static List<Field> getEntityFields(java.lang.Class<?> entityClass) {

		List<Field> fields = new ArrayList<Field>();
		if (!entityClass.equals(Object.class)) {
			for (Field field : Arrays.asList(entityClass.getDeclaredFields())) {
				if (!field.isAnnotationPresent(br.com.guerethes.orm.annotation.ddl.Transient.class)) {
					if (!field.getName().equals("serialVersionUID")) {
						fields.add(field);
					}
				}
			}
			List<Field> fields2 = getEntityFields(entityClass.getSuperclass());
			if (fields2.size() > 0) {
				fields.addAll(fields2);
			}
		} else {
			List<Field> lista = new ArrayList<Field>();
			for (Field field : Arrays.asList(entityClass.getDeclaredFields())) {
				if (!field
						.isAnnotationPresent(br.com.guerethes.orm.annotation.ddl.Transient.class)) {
					if (!field.getName().equals("serialVersionUID")) {
						lista.add(field);
					}
				}
			}
		}
		return fields;
	}

	public static Object getID(Object entity) {
		return FieldReflection.getValue(entity, entity.getClass(), "id");
	}

	public static Object getField(Object entity, String field) {
		if ( field.contains(".") ) {
			StringTokenizer st = new StringTokenizer(field, ".");
			Object object = entity;
			int lastObject = st.countTokens()-1;
			int token = 0;
			String attr = "";
			while (st.hasMoreElements()) {
				if ( token != lastObject )
					object = EntityReflection.getField(object, st.nextToken());
				else
					attr = st.nextToken();
			token++;
			}
			return FieldReflection.getValue(object, object.getClass(), attr);
		} else {
			return FieldReflection.getValue(entity, entity.getClass(), field);	
		}
	}

	public static HashMap<String, FieldValue> getFieldValues(Object entity) {

		HashMap<String, FieldValue> fields = new HashMap<String, FieldValue>();
		for (Field field : Arrays.asList(entity.getClass().getDeclaredFields())) {
			if (!field.isAnnotationPresent(br.com.guerethes.orm.annotation.ddl.Transient.class)) {
				if (!field.getName().equals("serialVersionUID")) {
					FieldValue fv = new FieldValue();
					fv.setField(field);
					fv.setValue(FieldReflection.getValue(entity,
							entity.getClass(), field.getName()));
					String columnName = FieldReflection.getColumnName(
							entity.getClass(), field.getName());
					fields.put(columnName, fv);
				}
			}
		}
		return fields;

	}
}
