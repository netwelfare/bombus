package cn.bombus.core.sql.node;

import cn.bombus.core.sql.DynamicContext;
import cn.bombus.core.sql.ExpressionEvaluator;

public class IfSqlNode implements SqlNode
{
	private ExpressionEvaluator evaluator;
	private String test;
	private SqlNode contents;

	public IfSqlNode(SqlNode contents, String test)
	{
		this.test = test;
		this.contents = contents;
		this.evaluator = new ExpressionEvaluator();
	}

	public boolean apply(DynamicContext context)
	{
		if (evaluator.evaluateBoolean(test, context.getBindings()))
		{
			contents.apply(context);
			return true;
		}
		return false;
	}

}
