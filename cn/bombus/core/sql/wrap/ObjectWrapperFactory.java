package cn.bombus.core.sql.wrap;

import cn.bombus.core.reflect.MetaObject;

public interface ObjectWrapperFactory {

	boolean hasWrapperFor(Object object);

	ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);

}
