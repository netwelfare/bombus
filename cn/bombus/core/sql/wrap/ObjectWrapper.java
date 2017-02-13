package cn.bombus.core.sql.wrap;

import cn.bombus.core.PropertyTokenizer;
import cn.bombus.core.reflect.MetaObject;
import cn.bombus.core.reflect.ObjectFactory;

public interface ObjectWrapper {

	Object get(PropertyTokenizer prop);

	void set(PropertyTokenizer prop, Object value);

	String findProperty(String name);

	String[] getGetterNames();

	String[] getSetterNames();

	Class<?> getSetterType(String name);

	Class<?> getGetterType(String name);

	boolean hasSetter(String name);

	boolean hasGetter(String name);

	MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);

}
