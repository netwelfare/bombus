package cn.bombus.core.sql.node;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bombus.core.exception.BuilderException;
import cn.bombus.core.sql.DynamicContext;
import cn.bombus.core.sql.token.GenericTokenParser;
import cn.bombus.core.sql.token.TokenHandler;
import cn.bombus.core.sql.type.SimpleTypeRegistry;
import ognl.Ognl;
import ognl.OgnlException;

public class TextSqlNode implements SqlNode
{
	private String text;

	public TextSqlNode(String text)
	{
		this.text = text;
	}

	public boolean apply(DynamicContext context)
	{
		if (text.contains("#{"))
		{
			GenericTokenParser parser = new GenericTokenParser("#{", "}", new BindingTokenParser(context));
			context.appendSql(parser.parse(text));
		}
		else if (text.contains("${"))
		{
			GenericTokenParser parser = new GenericTokenParser("${", "}", new BindingTokenParser(context));
			context.appendSql(parser.parse(text));
		}
		else
		{
			context.appendSql(text);
		}
		return true;
	}

	private static class BindingTokenParser implements TokenHandler
	{

		private DynamicContext context;

		public BindingTokenParser(DynamicContext context)
		{
			this.context = context;
		}

		public String handleToken(String content)
		{
			try
			{
				Object parameter = context.getBindings().get("_parameter");
				if (parameter == null)
				{
					context.getBindings().put("value", null);
				}
				else if (SimpleTypeRegistry.isSimpleType(parameter.getClass()))
				{
					context.getBindings().put("value", parameter);
				}
				Object value = Ognl.getValue(content, context.getBindings());
				//if (content.contains("#{"))
				{
					if (value instanceof String)
					{
						value = "'" + value + "'";
					}
				}
				if (value == null)
				{
					value = "?";
				}
				if (value instanceof Date)
				{
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					value = format.format(value);
					value = "date '" + value + "'";
				}
				return String.valueOf(value);
			}
			catch (OgnlException e)
			{
				throw new BuilderException("Error evaluating expression '" + content + "'. Cause: " + e, e);
			}
		}
	}

}
