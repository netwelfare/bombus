package cn.bombus.core.sql.type;

import java.util.HashMap;
import java.util.Map;

public final class TypeHandlerRegistry
{
	private final Map<Class<?>, Map<JdbcType, TypeHandler>> TYPE_HANDLER_MAP = new HashMap<Class<?>, Map<JdbcType, TypeHandler>>();
	//	private static final Map<Class<?>, Class<?>> reversePrimitiveMap = new HashMap<Class<?>, Class<?>>() {
	//		{
	//			put(Byte.class, byte.class);
	//			put(Short.class, short.class);
	//			put(Integer.class, int.class);
	//			put(Long.class, long.class);
	//			put(Float.class, float.class);
	//			put(Double.class, double.class);
	//			put(Boolean.class, boolean.class);
	//		}
	//	};

	public TypeHandler getTypeHandler(Class<?> type)
	{
		return getTypeHandler(type, null);
	}

	public TypeHandler getTypeHandler(Class<?> type, JdbcType jdbcType)
	{
		Map<JdbcType, TypeHandler> jdbcHandlerMap = TYPE_HANDLER_MAP.get(type);
		TypeHandler handler = null;
		if (jdbcHandlerMap != null)
		{
			handler = jdbcHandlerMap.get(jdbcType);
			if (handler == null)
			{
				handler = jdbcHandlerMap.get(null);
			}
		}
		if (handler == null && type != null && Enum.class.isAssignableFrom(type))
		{
			handler = null;
		}
		return handler;
	}

}
