package cn.bombus.core;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import cn.bombus.core.exception.BuilderException;
import cn.bombus.core.exception.IncompleteStatementException;
import cn.bombus.core.xml.XMLMapperEntityResolver;
import cn.bombus.core.xml.XNode;
import cn.bombus.core.xml.XPathParser;

public class XMLMapperBuilder extends BaseBuilder
{

	private XPathParser parser;
	private MapperBuilderAssistant builderAssistant;
	private Map<String, XNode> sqlFragments;
	private String resource;

	public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource,
			Map<String, XNode> sqlFragments)
	{

		this(new XPathParser(inputStream, true, configuration.getVariables(), new XMLMapperEntityResolver()),
				configuration, resource, sqlFragments);
	}

	private XMLMapperBuilder(XPathParser parser, Configuration configuration, String resource,
			Map<String, XNode> sqlFragments)
	{
		super(configuration);
		this.builderAssistant = new MapperBuilderAssistant(configuration, resource);
		this.parser = parser;
		this.sqlFragments = sqlFragments;
		this.resource = resource;
	}

	public void parse()
	{
		{
			XNode context = parser.evalNode("/mapper");
			typeAliasesElement(context.evalNode("typeAliases"));
			configurationElement(context);
			bindMapperForNamespace();
		}
	}

	public XNode getSqlFragment(String refid)
	{
		return sqlFragments.get(refid);
	}

	private void configurationElement(XNode context)
	{
		try
		{
			//命名空间
			String namespace = context.getStringAttribute("namespace");
			builderAssistant.setCurrentNamespace(namespace);
			//解析sql元素
			sqlElement(context.evalNodes("sql"));
			//解析sql节点
			List<XNode> list = context.evalNodes("select|insert|update|delete");
			//生成sql语句
			buildStatementFromContext(list);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error parsing Mapper XML. Cause: " + e, e);
		}
	}

	private void buildStatementFromContext(List<XNode> list)
	{
		for (XNode context : list)
		{
			final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant,
					context);
			try
			{
				statementParser.parseStatementNode();
			}
			catch (IncompleteStatementException e)
			{

			}
		}
	}

	private void sqlElement(List<XNode> list) throws Exception
	{
		for (XNode context : list)
		{
			String id = context.getStringAttribute("id");
			id = builderAssistant.applyCurrentNamespace(id);
			sqlFragments.put(id, context);
		}
	}

	private void bindMapperForNamespace()
	{
		String namespace = builderAssistant.getCurrentNamespace();
		if (namespace != null)
		{
			Class<?> boundType = null;
			try
			{
				boundType = Resources.classForName(namespace);
			}
			catch (ClassNotFoundException e)
			{
				//ignore, bound type is not required
			}
			if (boundType != null)
			{

			}
		}
	}

	private void typeAliasesElement(XNode parent)
	{
		if (parent != null)
		{
			for (XNode child : parent.getChildren())
			{
				String alias = child.getStringAttribute("alias");
				String type = child.getStringAttribute("type");
				try
				{
					Class<?> clazz = Resources.classForName(type);
					if (alias == null)
					{
						typeAliasRegistry.registerAlias(clazz);
					}
					else
					{
						typeAliasRegistry.registerAlias(alias, clazz);
					}
				}
				catch (ClassNotFoundException e)
				{
					throw new BuilderException("Error registering typeAlias for '" + alias + "'. Cause: " + e, e);
				}
			}
		}
	}

}
