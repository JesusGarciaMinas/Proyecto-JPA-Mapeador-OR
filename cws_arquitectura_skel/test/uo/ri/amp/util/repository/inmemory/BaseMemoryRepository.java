package uo.ri.amp.util.repository.inmemory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alb.util.reflection.ReflectionUtil;

public abstract class BaseMemoryRepository<T> {
	long counter = 0L;
	protected Map<Long, T> entities = new HashMap<>();

	public void add(T t) {
		Long l = nextId();
		putAttr(t, "id", l);
		entities.put(l, t);
	}

	private Long nextId() {
		return ++counter;
	}

	public void remove(T t) {
		Long id = getAttr(t, "id");
		entities.remove( id );
	}

	public T findById(Long id) {
		return entities.get( id );
	}

	public List<T> findAll() {
		return new ArrayList<>( entities.values() );
	}

	private void putAttr(T owner, String fieldName, Long value) {
		Field field = ReflectionUtil.getField(owner.getClass(), fieldName);
		ReflectionUtil.applyValueToField(owner, field, value);
	}

	private Long getAttr(T owner, String fieldName) {
		Field field = ReflectionUtil.getField(owner.getClass(), fieldName);
		return (Long) ReflectionUtil.getFieldValue(owner, field);
	}

}
