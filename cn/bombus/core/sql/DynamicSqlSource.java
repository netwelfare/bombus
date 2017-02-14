package cn.bombus.core.sql;

import java.util.Map;

import cn.bombus.core.Configuration;
import cn.bombus.core.sql.node.SqlNode;

public class DynamicSqlSource implements SqlSource
{
	private Configuration configuration;
	private SqlNode rootSqlNode;

	public DynamicSqlSource(Configuration configuration, SqlNode rootSqlNode)
	{
		this.configuration = configuration;
		this.rootSqlNode = rootSqlNode;
	}

	public BoundSql getBoundSql(Object parameterObject)
	{
		DynamicContext context = new DynamicContext(configuration, parameterObject);
		//开始遍历整个节点，一般都是不变（变化），而这种变化（不变）的逆向思维令人难以 理解
		rootSqlNode.apply(context);

		SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
		Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
		SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType);
		BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
		for (Map.Entry<String, Object> entry : context.getBindings().entrySet())
		{
			boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
		}
		return boundSql;
	}

}
