package cn.bombus.core.sql.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.bombus.core.Configuration;
import cn.bombus.core.MappedStatement;
import cn.bombus.core.sql.BoundSql;
import cn.bombus.core.sql.type.TypeHandlerRegistry;

public class DefaultParameterHandler implements ParameterHandler
{
	private final TypeHandlerRegistry typeHandlerRegistry;
	private final MappedStatement mappedStatement;
	private final Object parameterObject;
	private BoundSql boundSql;
	private Configuration configuration;

	public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql)
	{
		this.mappedStatement = mappedStatement;
		this.configuration = mappedStatement.getConfiguration();
		this.typeHandlerRegistry = null;
		this.parameterObject = parameterObject;
		this.boundSql = boundSql;
	}

	public Object getParameterObject()
	{
		return parameterObject;
	}

	public void setParameters(PreparedStatement ps) throws SQLException
	{

	}

}
