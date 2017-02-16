package cn.bombus.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.bombus.core.exception.BuilderException;
import cn.bombus.core.exception.IncompleteStatementException;
import cn.bombus.core.sql.DynamicSqlSource;
import cn.bombus.core.sql.SqlCommandType;
import cn.bombus.core.sql.SqlSource;
import cn.bombus.core.sql.key.KeyGenerator;
import cn.bombus.core.sql.key.NoKeyGenerator;
import cn.bombus.core.sql.key.SelectKeyGenerator;
import cn.bombus.core.sql.node.ChooseSqlNode;
import cn.bombus.core.sql.node.ForEachSqlNode;
import cn.bombus.core.sql.node.IfSqlNode;
import cn.bombus.core.sql.node.MixedSqlNode;
import cn.bombus.core.sql.node.SetSqlNode;
import cn.bombus.core.sql.node.SqlNode;
import cn.bombus.core.sql.node.TextSqlNode;
import cn.bombus.core.sql.node.TrimSqlNode;
import cn.bombus.core.sql.node.WhereSqlNode;
import cn.bombus.core.sql.result.ResultSetType;
import cn.bombus.core.xml.XNode;

public class XMLStatementBuilder extends BaseBuilder
{
	private MapperBuilderAssistant builderAssistant;
	private XNode context;

	public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context)
	{
		super(configuration);
		this.builderAssistant = builderAssistant;
		this.context = context;
	}

	//针对单个sql语句进行处理的：select|insert|update|delete
	public void parseStatementNode()
	{
		String id = context.getStringAttribute("id");
		Integer fetchSize = null;
		Integer timeout = null;
		String parameterMap = null;
		Class<?> parameterTypeClass = null;
		String resultMap = null;
		Class<?> resultTypeClass = null;
		StatementType statementType = StatementType
				.valueOf(context.getStringAttribute("statementType", StatementType.PREPARED.toString()));
		ResultSetType resultSetTypeEnum = null;
		List<SqlNode> contents = parseDynamicTags(context);
		MixedSqlNode rootSqlNode = new MixedSqlNode(contents);
		SqlSource sqlSource = new DynamicSqlSource(configuration, rootSqlNode);
		String nodeName = context.getNode().getNodeName();
		SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
		boolean flushCache = false;
		boolean useCache = false;
		String keyProperty = null;
		KeyGenerator keyGenerator = new NoKeyGenerator();
		builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout,
				parameterMap, parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache,
				keyGenerator, keyProperty);
	}

	public String getStatementIdWithNameSpace()
	{
		return builderAssistant.applyCurrentNamespace(context.getStringAttribute("id"));
	}

	/*<select parameterType="map" id="getStatisUserGroups">
	<include refid="userColumns"/>
	<choose>
	<when test="statFrequency=='HOUR'">hour,</when>
	<when test="statFrequency=='DAY'">date,</when>
	</choose>
	<if test="pageNames!=null and pageNames.size()>0">page_name pageName,</if>
	<choose>
	<when test="pageNames!=null and pageNames.size()>0 and statFrequency=='DAY'">tb_data_statis_usergroup_page_day</when>
	<when test="pageNames!=null and pageNames.size()>0 and statFrequency=='HOUR'">tb_data_statis_usergroup_page_hour</when>
	<when test="(pageNames==null or pageNames.size()==0) and statFrequency=='DAY'">tb_data_statis_usergroup_indicator_day</when>
	<when test="(pageNames==null or pageNames.size()==0) and statFrequency=='HOUR'">tb_data_statis_usergroup_indicator_hour</when>
	</choose>*/

	private List<SqlNode> parseDynamicTags(XNode node)
	{
		List<SqlNode> contents = new ArrayList<SqlNode>();
		NodeList children = node.getNode().getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			XNode child = node.newXNode(children.item(i));
			String nodeName = child.getNode().getNodeName();
			if (child.getNode().getNodeType() == Node.CDATA_SECTION_NODE
					|| child.getNode().getNodeType() == Node.TEXT_NODE)
			{
				String data = child.getStringBody("");
				contents.add(new TextSqlNode(data));
			}
			else
			{
				NodeHandler handler = nodeHandlers.get(nodeName);
				if (handler == null)
				{
					throw new BuilderException("Unknown element <" + nodeName + "> in SQL statement.");
				}
				handler.handleNode(child, contents);

			}
		}
		return contents;
	}

	private Map<String, NodeHandler> nodeHandlers = new HashMap<String, NodeHandler>() {
		private static final long serialVersionUID = 7123056019193266281L;
		{
			put("include", new IncludeNodeHandler());
			put("trim", new TrimHandler());
			put("where", new WhereHandler());
			put("set", new SetHandler());
			put("foreach", new ForEachHandler());
			put("if", new IfHandler());
			put("choose", new ChooseHandler());
			put("when", new IfHandler());
			put("otherwise", new OtherwiseHandler());
			put("selectKey", new SelectKeyHandler());
		}
	};

	private interface NodeHandler
	{
		void handleNode(XNode nodeToHandle, List<SqlNode> targetContents);
	}

	private class SelectKeyHandler implements NodeHandler
	{
		public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents)
		{
			XNode parent = nodeToHandle.getParent();
			String id = parent.getStringAttribute("id") + SelectKeyGenerator.SELECT_KEY_SUFFIX;
			String resultType = nodeToHandle.getStringAttribute("resultType");
			Class<?> resultTypeClass = resolveClass(resultType);
			StatementType statementType = StatementType
					.valueOf(nodeToHandle.getStringAttribute("statementType", StatementType.PREPARED.toString()));
			String keyProperty = nodeToHandle.getStringAttribute("keyProperty");
			String parameterType = parent.getStringAttribute("parameterType");
			boolean executeBefore = "BEFORE".equals(nodeToHandle.getStringAttribute("order", "AFTER"));
			Class<?> parameterTypeClass = resolveClass(parameterType);

			// defaults
			boolean useCache = false;
			KeyGenerator keyGenerator = new NoKeyGenerator();
			Integer fetchSize = null;
			Integer timeout = null;
			boolean flushCache = false;
			String parameterMap = null;
			String resultMap = null;
			ResultSetType resultSetTypeEnum = null;

			List<SqlNode> contents = parseDynamicTags(nodeToHandle);
			MixedSqlNode rootSqlNode = new MixedSqlNode(contents);
			SqlSource sqlSource = new DynamicSqlSource(configuration, rootSqlNode);
			SqlCommandType sqlCommandType = SqlCommandType.SELECT;

			builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout,
					parameterMap, parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache,
					useCache, keyGenerator, keyProperty);

			id = builderAssistant.applyCurrentNamespace(id);

			MappedStatement keyStatement = configuration.getMappedStatement(id, false);
			// configuration.addKeyGenerator(id, new
			// SelectKeyGenerator(keyStatement, executeBefore));
		}
	}

	private class IncludeNodeHandler implements NodeHandler
	{
		public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents)
		{
			String refid = nodeToHandle.getStringAttribute("refid");
			refid = builderAssistant.applyCurrentNamespace(refid);
			try
			{
				XNode includeNode = configuration.getSqlFragments().get(refid);
				if (includeNode == null)
				{
					String nsrefid = builderAssistant.applyCurrentNamespace(refid);
					includeNode = configuration.getSqlFragments().get(nsrefid);
					if (includeNode == null)
					{
						throw new IncompleteStatementException(
								"Could not find SQL statement to include with refid '" + refid + "'");
					}
				}
				MixedSqlNode mixedSqlNode = new MixedSqlNode(contents(includeNode));
				targetContents.add(mixedSqlNode);
			}
			catch (IllegalArgumentException e)
			{
				throw new IncompleteStatementException(
						"Could not find SQL statement to include with refid '" + refid + "'", e);
			}
		}

		private List<SqlNode> contents(XNode includeNode)
		{//内部类使用外部类的私有函数
			return parseDynamicTags(includeNode);
		}
	}

	private class TrimHandler implements NodeHandler
	{
		public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents)
		{
			List<SqlNode> contents = parseDynamicTags(nodeToHandle);
			MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
			String prefix = nodeToHandle.getStringAttribute("prefix");
			String prefixOverrides = nodeToHandle.getStringAttribute("prefixOverrides");
			String suffix = nodeToHandle.getStringAttribute("suffix");
			String suffixOverrides = nodeToHandle.getStringAttribute("suffixOverrides");
			TrimSqlNode trim = new TrimSqlNode(configuration, mixedSqlNode, prefix, prefixOverrides, suffix,
					suffixOverrides);
			targetContents.add(trim);
		}
	}

	private class WhereHandler implements NodeHandler
	{
		public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents)
		{
			List<SqlNode> contents = parseDynamicTags(nodeToHandle);
			MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
			WhereSqlNode where = new WhereSqlNode(configuration, mixedSqlNode);
			targetContents.add(where);
		}
	}

	private class SetHandler implements NodeHandler
	{
		public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents)
		{
			List<SqlNode> contents = parseDynamicTags(nodeToHandle);
			MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
			SetSqlNode set = new SetSqlNode(configuration, mixedSqlNode);
			targetContents.add(set);
		}
	}

	private class ForEachHandler implements NodeHandler
	{
		public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents)
		{
			List<SqlNode> contents = parseDynamicTags(nodeToHandle);
			MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
			String collection = nodeToHandle.getStringAttribute("collection");
			String item = nodeToHandle.getStringAttribute("item");
			String index = nodeToHandle.getStringAttribute("index");
			String open = nodeToHandle.getStringAttribute("open");
			String close = nodeToHandle.getStringAttribute("close");
			String separator = nodeToHandle.getStringAttribute("separator");
			ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration, mixedSqlNode, collection, index, item,
					open, close, separator);
			targetContents.add(forEachSqlNode);
		}
	}

	private class IfHandler implements NodeHandler
	{
		public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents)
		{
			List<SqlNode> contents = parseDynamicTags(nodeToHandle);
			MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
			String test = nodeToHandle.getStringAttribute("test");
			IfSqlNode ifSqlNode = new IfSqlNode(mixedSqlNode, test);
			targetContents.add(ifSqlNode);
		}
	}

	private class OtherwiseHandler implements NodeHandler
	{
		public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents)
		{
			List<SqlNode> contents = parseDynamicTags(nodeToHandle);
			MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
			targetContents.add(mixedSqlNode);
		}
	}

	private class ChooseHandler implements NodeHandler
	{
		public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents)
		{
			List whenSqlNodes = new ArrayList<SqlNode>();
			List<SqlNode> otherwiseSqlNodes = new ArrayList<SqlNode>();
			handleWhenOtherwiseNodes(nodeToHandle, whenSqlNodes, otherwiseSqlNodes);
			SqlNode defaultSqlNode = getDefaultSqlNode(otherwiseSqlNodes);
			ChooseSqlNode chooseSqlNode = new ChooseSqlNode((List<IfSqlNode>) whenSqlNodes, defaultSqlNode);
			targetContents.add(chooseSqlNode);
		}

		private void handleWhenOtherwiseNodes(XNode chooseSqlNode, List<SqlNode> ifSqlNodes,
				List<SqlNode> defaultSqlNodes)
		{
			List<XNode> children = chooseSqlNode.getChildren();
			for (XNode child : children)
			{
				String nodeName = child.getNode().getNodeName();
				NodeHandler handler = nodeHandlers.get(nodeName);
				if (handler instanceof IfHandler)
				{
					handler.handleNode(child, ifSqlNodes);
				}
				else if (handler instanceof OtherwiseHandler)
				{
					handler.handleNode(child, defaultSqlNodes);
				}
			}
		}

		private SqlNode getDefaultSqlNode(List<SqlNode> defaultSqlNodes)
		{
			SqlNode defaultSqlNode = null;
			if (defaultSqlNodes.size() == 1)
			{
				defaultSqlNode = defaultSqlNodes.get(0);
			}
			else if (defaultSqlNodes.size() > 1)
			{
				throw new BuilderException("Too many default (otherwise) elements in choose statement.");
			}
			return defaultSqlNode;
		}
	}

}
