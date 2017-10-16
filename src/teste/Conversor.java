package teste;

import java.lang.reflect.Field;

public class Conversor<T> {
	
	@SuppressWarnings("unchecked")
	public T convertToObject(final Class<T> clazz, final String[] values) throws Exception {
		final Class<T> newClass = (Class<T>) Class.forName(clazz.getName()); 
		final T newObject = newClass.newInstance();
		int index = 0;
		for (Field field : newObject.getClass().getDeclaredFields()) {
			field.setAccessible(Boolean.TRUE);
			final Class<?> typeClass = (Class<?>) Class.forName(field.getType().getName());
			final Object typeObject = typeClass.getDeclaredConstructor(String.class).newInstance(values[index]);
			field.set(newObject, typeObject);
			index++;
		}
		return newObject;
	}
	
}
