package cn.bombus.core.sql.key;

import java.sql.Statement;
import java.util.concurrent.Executor;

import cn.bombus.core.Configuration;
import cn.bombus.core.MappedStatement;
import cn.bombus.core.exception.ExecutorException;

public class SelectKeyGenerator implements KeyGenerator
{
	public static final String SELECT_KEY_SUFFIX = "!selectKey";
	private boolean executeBefore;
	private MappedStatement keyStatement;

	public SelectKeyGenerator(MappedStatement keyStatement, boolean executeBefore)
	{
		this.executeBefore = executeBefore;
		this.keyStatement = keyStatement;
	}

	public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter)
	{
		if (executeBefore)
		{
			processGeneratedKeys(executor, ms, stmt, parameter);
		}
	}

	public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter)
	{
		if (!executeBefore)
		{
			processGeneratedKeys(executor, ms, stmt, parameter);
		}
	}

	private void processGeneratedKeys(Executor executor, MappedStatement ms, Statement stmt, Object parameter)
	{
		try
		{
			final Configuration configuration = ms.getConfiguration();
			if (parameter != null)
			{
				String keyStatementName = ms.getId() + SELECT_KEY_SUFFIX;

			}
		}
		catch (Exception e)
		{
			throw new ExecutorException("Error selecting key or setting result to parameter object. Cause: " + e, e);
		}
	}

}
