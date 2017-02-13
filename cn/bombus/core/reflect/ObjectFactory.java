package cn.bombus.core.reflect;

import java.util.List;
import java.util.Properties;

public interface ObjectFactory {

	Object create(Class type);

	Object create(Class type, List<Class> constructorArgTypes, List<Object> constructorArgs);

	void setProperties(Properties properties);

}
