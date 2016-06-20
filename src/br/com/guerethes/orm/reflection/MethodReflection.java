package br.com.guerethes.orm.reflection;

import java.lang.reflect.Method;


public final class MethodReflection {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean haAnnotation(Class<?> targetClass, Class annotation) {
		for (Method method : targetClass.getMethods()) {
			if (method.isAnnotationPresent(annotation))
				return true;
		}
		
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Method getMethodAnnotation(Class<?> targetClass, Class annotation) {
		for (Method method : targetClass.getMethods()) {
			if (method.isAnnotationPresent(annotation))
				return method;
		}
		return null;
	}
	
}