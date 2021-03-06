package cn.bombus.core;

import cn.bombus.core.exception.BuilderException;
import cn.bombus.core.sql.parameter.ParameterMode;
import cn.bombus.core.sql.result.ResultSetType;
import cn.bombus.core.sql.type.JdbcType;
import cn.bombus.core.sql.type.TypeAliasRegistry;

public abstract class BaseBuilder
{
	/**
	 * 最基本的一个类，需要configuration，需要别名。这是顺其自然的东西
	 */
	protected final Configuration configuration;
	protected final TypeAliasRegistry typeAliasRegistry;

	public BaseBuilder(Configuration configuration)
	{
		this.configuration = configuration;
		this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
	}

	public Configuration getConfiguration()
	{
		return configuration;
	}

	protected String stringValueOf(String value, String defaultValue)
	{
		return value == null ? defaultValue : value;
	}

	protected Boolean booleanValueOf(String value, Boolean defaultValue)
	{
		return value == null ? defaultValue : Boolean.valueOf(value);
	}

	protected Integer integerValueOf(String value, Integer defaultValue)
	{
		return value == null ? defaultValue : Integer.valueOf(value);
	}

	protected ResultSetType resolveResultSetType(String alias)
	{
		if (alias == null)
		{
			return null;
		}
		try
		{
			return ResultSetType.valueOf(alias);
		}
		catch (IllegalArgumentException e)
		{
			throw new BuilderException("Error resolving ResultSetType. Cause: " + e, e);
		}
	}

	protected Class<?> resolveClass(String alias)
	{
		if (alias == null)
		{
			return null;
		}
		try
		{
			return resolveAlias(alias);
		}
		catch (Exception e)
		{
			throw new BuilderException("Error resolving class . Cause: " + e, e);
		}
	}

	protected Object resolveInstance(String alias)
	{
		if (alias == null)
			return null;
		try
		{
			Class<?> type = resolveClass(alias);
			return type.newInstance();
		}
		catch (Exception e)
		{
			throw new BuilderException("Error instantiating class. Cause: " + e, e);
		}
	}

	protected Object resolveInstance(Class<?> type)
	{
		if (type == null)
		{
			return null;
		}
		try
		{
			return type.newInstance();
		}
		catch (Exception e)
		{
			throw new BuilderException("Error instantiating class. Cause: " + e, e);
		}
	}

	protected Class<?> resolveAlias(String alias)
	{
		return typeAliasRegistry.resolveAlias(alias);
	}

	protected JdbcType resolveJdbcType(String alias)
	{
		if (alias == null)
		{
			return null;
		}
		try
		{
			return JdbcType.valueOf(alias);
		}
		catch (IllegalArgumentException e)
		{
			throw new BuilderException("Error resolving JdbcType. Cause: " + e, e);
		}
	}

	protected ParameterMode resolveParameterMode(String alias)
	{
		if (alias == null)
		{
			return null;
		}
		try
		{
			return ParameterMode.valueOf(alias);
		}
		catch (IllegalArgumentException e)
		{
			throw new BuilderException("Error resolving ParameterMode. Cause: " + e, e);
		}
	}
}
