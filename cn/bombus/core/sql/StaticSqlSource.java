package cn.bombus.core.sql;

import java.util.List;

import cn.bombus.core.Configuration;
import cn.bombus.core.sql.parameter.ParameterMapping;

public class StaticSqlSource implements SqlSource
{

	private String sql;
	private List<ParameterMapping> parameterMappings;
	private Configuration configuration;

	public StaticSqlSource(Configuration configuration, String sql)
	{
		this(configuration, sql, null);
	}

	public StaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings)
	{
		this.sql = sql;
		this.parameterMappings = parameterMappings;
		this.configuration = configuration;
	}

	public BoundSql getBoundSql(Object parameterObject)
	{
		return new BoundSql(configuration, sql, parameterMappings, parameterObject);
	}

}
