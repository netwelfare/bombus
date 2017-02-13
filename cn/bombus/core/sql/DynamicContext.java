package cn.bombus.core.sql;

import java.util.HashMap;
import java.util.Map;

import cn.bombus.core.Configuration;
import cn.bombus.core.reflect.MetaObject;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

public class DynamicContext
{
	/*	
	 在DynamicContext的构造函数中，可以看到，根据传入的参数对象是否为Map类型，有两个不同构造ContextMap的方式。而ContextMap作为一个继承了HashMap的对象，
	作用就是用于统一参数的访问方式：用Map接口方法来访问数据。具体来说，当传入的参数对象不是Map类型时，Mybatis会将传入的POJO对象用MetaObject对象来封装，
	当动态计算sql过程需要获取数据时，用Map接口的get方法包装 MetaObject对象的取值过程。
	*/

	/*ContextAccessor也是DynamicContext的内部类，实现了Ognl中的PropertyAccessor接口，为Ognl提供了如何使用ContextMap参数对象的说明，
	 * 这个类也为整个参数对象“map”化划上了最后一笔。
	*/
	public static final String PARAMETER_OBJECT_KEY = "_parameter";

	static
	{
		OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
	}

	private final ContextMap bindings;
	private final StringBuilder sqlBuilder = new StringBuilder();
	private int uniqueNumber = 0;

	public DynamicContext(Configuration configuration, Object parameterObject)
	{
		if (parameterObject != null && !(parameterObject instanceof Map))
		{
			MetaObject metaObject = configuration.newMetaObject(parameterObject);
			bindings = new ContextMap(metaObject);
		}
		else
		{
			bindings = new ContextMap(null);
		}
		bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
	}

	public Map<String, Object> getBindings()
	{
		return bindings;
	}

	public void bind(String name, Object value)
	{
		bindings.put(name, value);
	}

	public void appendSql(String sql)
	{
		sqlBuilder.append(sql);
		sqlBuilder.append(" ");
	}

	public String getSql()
	{
		return sqlBuilder.toString().trim();
	}

	public int getUniqueNumber()
	{
		return uniqueNumber++;
	}

	static class ContextMap extends HashMap<String, Object>
	{
		private static final long serialVersionUID = 2977601501966151582L;

		private MetaObject parameterMetaObject;

		public ContextMap(MetaObject parameterMetaObject)
		{
			this.parameterMetaObject = parameterMetaObject;
		}

		@Override
		public Object get(Object key)
		{
			if (super.containsKey(key))
			{
				return super.get(key);
			}

			if (parameterMetaObject != null)
			{
				Object object = parameterMetaObject.getValue(key.toString());
				if (object != null)
				{
					super.put(key.toString(), object);
				}

				return object;
			}

			return null;
		}
	}

	static class ContextAccessor implements PropertyAccessor
	{

		public Object getProperty(Map context, Object target, Object name) throws OgnlException
		{
			Map map = (Map) target;

			Object result = map.get(name);
			if (result != null)
			{
				return result;
			}

			Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
			if (parameterObject instanceof Map)
			{
				return ((Map) parameterObject).get(name);
			}

			return null;
		}

		public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException
		{
			Map map = (Map) target;
			map.put(name, value);
		}

		public String getSourceAccessor(OgnlContext arg0, Object arg1, Object arg2)
		{
			// TODO Auto-generated method stub
			return null;
		}

		public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2)
		{
			// TODO Auto-generated method stub
			return null;
		}
	}
}
