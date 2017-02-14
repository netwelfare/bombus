package cn.bombus.core.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bombus.core.Configuration;
import cn.bombus.core.reflect.MetaObject;
import cn.bombus.core.sql.parameter.ParameterMapping;

public class BoundSql
{
	private String sql;
	private List<ParameterMapping> parameterMappings;
	private Object parameterObject;
	private Map<String, Object> additionalParameters;
	private MetaObject metaParameters;

	public BoundSql(Configuration configuration, String sql, List<ParameterMapping> parameterMappings,
			Object parameterObject)
	{
		this.sql = sql;
		this.parameterMappings = parameterMappings;
		this.parameterObject = parameterObject;
		this.additionalParameters = new HashMap<String, Object>();
		this.metaParameters = configuration.newMetaObject(additionalParameters);
	}

	public String getSql()
	{
		return sql;
	}

	public List<ParameterMapping> getParameterMappings()
	{
		return parameterMappings;
	}

	public Object getParameterObject()
	{
		return parameterObject;
	}

	public boolean hasAdditionalParameter(String name)
	{
		return metaParameters.hasGetter(name);
	}

	public void setAdditionalParameter(String name, Object value)
	{
		metaParameters.setValue(name, value);
	}

	public Object getAdditionalParameter(String name)
	{
		return metaParameters.getValue(name);
	}
}
