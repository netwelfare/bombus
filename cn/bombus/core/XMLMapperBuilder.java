package cn.bombus.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.bombus.core.exception.BuilderException;
import cn.bombus.core.exception.IncompleteStatementException;
import cn.bombus.core.xml.XMLMapperEntityResolver;
import cn.bombus.core.xml.XNode;
import cn.bombus.core.xml.XPathParser;

public class XMLMapperBuilder extends BaseBuilder {

	private XPathParser parser;
	private MapperBuilderAssistant builderAssistant;
	private Map<String, XNode> sqlFragments;
	private String resource;

	// @Deprecated
	// public XMLMapperBuilder(Reader reader, Configuration configuration,
	// String resource,
	// Map<String, XNode> sqlFragments, String namespace)
	// {
	// this(reader, configuration, resource, sqlFragments);
	// this.builderAssistant.setCurrentNamespace(namespace);
	// }
	//
	// @Deprecated
	// public XMLMapperBuilder(Reader reader, Configuration configuration,
	// String resource,
	// Map<String, XNode> sqlFragments)
	// {
	// this(new XPathParser(reader, true, configuration.getVariables(), new
	// XMLMapperEntityResolver()), configuration,
	// resource, sqlFragments);
	// }

	// public XMLMapperBuilder(InputStream inputStream, Configuration
	// configuration, String resource,
	// Map<String, XNode> sqlFragments, String namespace)
	// {
	// this(inputStream, configuration, resource, sqlFragments);
	// this.builderAssistant.setCurrentNamespace(namespace);
	// }

	public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource,
			Map<String, XNode> sqlFragments) {

		this(new XPathParser(inputStream, true, configuration.getVariables(), new XMLMapperEntityResolver()),
				configuration, resource, sqlFragments);
	}

	private XMLMapperBuilder(XPathParser parser, Configuration configuration, String resource,
			Map<String, XNode> sqlFragments) {
		super(configuration);
		this.builderAssistant = new MapperBuilderAssistant(configuration, resource);
		this.parser = parser;
		this.sqlFragments = sqlFragments;
		this.resource = resource;
	}

	public void parse() {
		// if (!configuration.isResourceLoaded(resource))
		{
			XNode context = parser.evalNode("/mapper");

			typeAliasesElement(context.evalNode("typeAliases"));
			configurationElement(context);
			// configuration.addLoadedResource(resource);
			bindMapperForNamespace();
		}

		// parsePendingChacheRefs();
		// parsePendingStatements();
	}

	public XNode getSqlFragment(String refid) {
		return sqlFragments.get(refid);
	}

	private void configurationElement(XNode context) {
		try {
			// String namespace = context.getStringAttribute("namespace");
			// builderAssistant.setCurrentNamespace(namespace);
			// cacheRefElement(context.evalNode("cache-ref"));
			// cacheElement(context.evalNode("cache"));
			// parameterMapElement(context.evalNodes("/mapper/parameterMap"));
			// resultMapElements(context.evalNodes("/mapper/resultMap"));
			sqlElement(context.evalNodes("sql"));
			// System.err.println(context.toString());
			List<XNode> list = context.evalNodes("select|insert|update|delete");
			buildStatementFromContext(list);
		} catch (Exception e) {
			throw new RuntimeException("Error parsing Mapper XML. Cause: " + e, e);
		}
	}

	private void buildStatementFromContext(List<XNode> list) {
		for (XNode context : list) {
			final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant,
					context);
			try {
				statementParser.parseStatementNode();
			} catch (IncompleteStatementException e) {

			}
		}
	}

	// private void parsePendingStatements()
	// {
	// Collection<XMLStatementBuilder> incompleteStatements =
	// configuration.getIncompleteStatements();
	// synchronized (incompleteStatements)
	// {
	// Iterator<XMLStatementBuilder> iter = incompleteStatements.iterator();
	// while (iter.hasNext())
	// {
	// try
	// {
	// iter.next().parseStatementNode();
	// iter.remove();
	// }
	// catch (IncompleteStatementException e)
	// {
	// // Statement is still missing a resource...
	// }
	// }
	// }
	// }

	// private void cacheElement(XNode context) throws Exception
	// {//Ω‚Œˆª∫¥Ê±Í«©
	// if (context != null)
	// {
	// String type = context.getStringAttribute("type", "PERPETUAL");
	// Class<? extends Cache> typeClass = typeAliasRegistry.resolveAlias(type);
	// String eviction = context.getStringAttribute("eviction", "LRU");
	// Class<? extends Cache> evictionClass =
	// typeAliasRegistry.resolveAlias(eviction);
	// Long flushInterval = context.getLongAttribute("flushInterval");
	// Integer size = context.getIntAttribute("size");
	// boolean readWrite = !context.getBooleanAttribute("readOnly", false);
	// Properties props = context.getChildrenAsProperties();
	// //builderAssistant.useNewCache(typeClass, evictionClass, flushInterval,
	// size, readWrite, props);
	// }
	// }

	// private void parameterMapElement(List<XNode> list) throws Exception
	// {
	// for (XNode parameterMapNode : list)
	// {
	// String id = parameterMapNode.getStringAttribute("id");
	// String type = parameterMapNode.getStringAttribute("type");
	// Class<?> parameterClass = resolveClass(type);
	// List<XNode> parameterNodes = parameterMapNode.evalNodes("parameter");
	// List<ParameterMapping> parameterMappings = new
	// ArrayList<ParameterMapping>();
	// for (XNode parameterNode : parameterNodes)
	// {
	// String property = parameterNode.getStringAttribute("property");
	// String javaType = parameterNode.getStringAttribute("javaType");
	// String jdbcType = parameterNode.getStringAttribute("jdbcType");
	// String resultMap = parameterNode.getStringAttribute("resultMap");
	// String mode = parameterNode.getStringAttribute("mode");
	// String typeHandler = parameterNode.getStringAttribute("typeHandler");
	// Integer numericScale = parameterNode.getIntAttribute("numericScale",
	// null);
	// ParameterMode modeEnum = resolveParameterMode(mode);
	// Class<?> javaTypeClass = resolveClass(javaType);
	// JdbcType jdbcTypeEnum = resolveJdbcType(jdbcType);
	// Class<? extends TypeHandler> typeHandlerClass = (Class<? extends
	// TypeHandler>) resolveClass(
	// typeHandler);
	// ParameterMapping parameterMapping =
	// builderAssistant.buildParameterMapping(parameterClass, property,
	// javaTypeClass, jdbcTypeEnum, resultMap, modeEnum, typeHandlerClass,
	// numericScale);
	// parameterMappings.add(parameterMapping);
	// }
	// builderAssistant.addParameterMap(id, parameterClass, parameterMappings);
	// }
	// }
	//
	// private void resultMapElements(List<XNode> list) throws Exception
	// {
	// for (XNode resultMapNode : list)
	// {
	// resultMapElement(resultMapNode);
	// }
	// }

	// private ResultMap resultMapElement(XNode resultMapNode) throws Exception
	// {
	// return resultMapElement(resultMapNode, Collections.EMPTY_LIST);
	// }

	// private ResultMap resultMapElement(XNode resultMapNode,
	// List<ResultMapping> additionalResultMappings)
	// throws Exception
	// {
	// ErrorContext.instance().activity("processing " +
	// resultMapNode.getValueBasedIdentifier());
	// String id = resultMapNode.getStringAttribute("id",
	// resultMapNode.getValueBasedIdentifier());
	// String type = resultMapNode.getStringAttribute("type",
	// resultMapNode.getStringAttribute("ofType",
	// resultMapNode.getStringAttribute("resultType",
	// resultMapNode.getStringAttribute("javaType"))));
	// String extend = resultMapNode.getStringAttribute("extends");
	// Class<?> typeClass = resolveClass(type);
	// Discriminator discriminator = null;
	// List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
	// resultMappings.addAll(additionalResultMappings);
	// List<XNode> resultChildren = resultMapNode.getChildren();
	// for (XNode resultChild : resultChildren)
	// {
	// if ("constructor".equals(resultChild.getName()))
	// {
	// processConstructorElement(resultChild, typeClass, resultMappings);
	// }
	// else if ("discriminator".equals(resultChild.getName()))
	// {
	// discriminator = processDiscriminatorElement(resultChild, typeClass,
	// resultMappings);
	// }
	// else
	// {
	// ArrayList<ResultFlag> flags = new ArrayList<ResultFlag>();
	// if ("id".equals(resultChild.getName()))
	// {
	// flags.add(ResultFlag.ID);
	// }
	// resultMappings.add(buildResultMappingFromContext(resultChild, typeClass,
	// flags));
	// }
	// }
	// return builderAssistant.addResultMap(id, typeClass, extend,
	// discriminator, resultMappings);
	// }
	//
	// private void processConstructorElement(XNode resultChild, Class<?>
	// resultType, List<ResultMapping> resultMappings)
	// throws Exception
	// {
	// List<XNode> argChildren = resultChild.getChildren();
	// for (XNode argChild : argChildren)
	// {
	// ArrayList<ResultFlag> flags = new ArrayList<ResultFlag>();
	// flags.add(ResultFlag.CONSTRUCTOR);
	// if ("idArg".equals(argChild.getName()))
	// {
	// flags.add(ResultFlag.ID);
	// }
	// resultMappings.add(buildResultMappingFromContext(argChild, resultType,
	// flags));
	// }
	// }

	// private Discriminator processDiscriminatorElement(XNode context, Class<?>
	// resultType,
	// List<ResultMapping> resultMappings) throws Exception
	// {
	// String column = context.getStringAttribute("column");
	// String javaType = context.getStringAttribute("javaType");
	// String jdbcType = context.getStringAttribute("jdbcType");
	// String typeHandler = context.getStringAttribute("typeHandler");
	// Class<?> javaTypeClass = resolveClass(javaType);
	// Class<? extends TypeHandler> typeHandlerClass = (Class<? extends
	// TypeHandler>) resolveClass(typeHandler);
	// JdbcType jdbcTypeEnum = resolveJdbcType(jdbcType);
	// Map<String, String> discriminatorMap = new HashMap<String, String>();
	// for (XNode caseChild : context.getChildren())
	// {
	// String value = caseChild.getStringAttribute("value");
	// String resultMap = caseChild.getStringAttribute("resultMap",
	// processNestedResultMappings(caseChild, resultMappings));
	// discriminatorMap.put(value, resultMap);
	// }
	// return builderAssistant.buildDiscriminator(resultType, column,
	// javaTypeClass, jdbcTypeEnum, typeHandlerClass,
	// discriminatorMap);
	// }

	 private void sqlElement(List<XNode> list) throws Exception
	 {
	 for (XNode context : list)
	 {
	 String id = context.getStringAttribute("id");
	 id = builderAssistant.applyCurrentNamespace(id);
	 sqlFragments.put(id, context);
	 }
	 }

	// private ResultMapping buildResultMappingFromContext(XNode context,
	// Class<?> resultType, ArrayList<ResultFlag> flags)
	// throws Exception
	// {
	// String property = context.getStringAttribute("property");
	// String column = context.getStringAttribute("column");
	// String javaType = context.getStringAttribute("javaType");
	// String jdbcType = context.getStringAttribute("jdbcType");
	// String nestedSelect = context.getStringAttribute("select");
	// String nestedResultMap = context.getStringAttribute("resultMap",
	// processNestedResultMappings(context, Collections.EMPTY_LIST));
	// String typeHandler = context.getStringAttribute("typeHandler");
	// Class<?> javaTypeClass = resolveClass(javaType);
	// Class<? extends TypeHandler> typeHandlerClass = (Class<? extends
	// TypeHandler>) resolveClass(typeHandler);
	// JdbcType jdbcTypeEnum = resolveJdbcType(jdbcType);
	// return builderAssistant.buildResultMapping(resultType, property, column,
	// javaTypeClass, jdbcTypeEnum,
	// nestedSelect, nestedResultMap, typeHandlerClass, flags);
	// }

	// private String processNestedResultMappings(XNode context,
	// List<ResultMapping> resultMappings) throws Exception
	// {
	// if ("association".equals(context.getName()) ||
	// "collection".equals(context.getName())
	// || "case".equals(context.getName()))
	// {
	// if (context.getStringAttribute("select") == null)
	// {
	// ResultMap resultMap = resultMapElement(context, resultMappings);
	// return resultMap.getId();
	// }
	// }
	// return null;
	// }

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

	private void typeAliasesElement(XNode parent) {
		if (parent != null) {
			for (XNode child : parent.getChildren()) {
				String alias = child.getStringAttribute("alias");
				String type = child.getStringAttribute("type");
				try {
					Class<?> clazz = Resources.classForName(type);
					if (alias == null) {
						typeAliasRegistry.registerAlias(clazz);
					} else {
						typeAliasRegistry.registerAlias(alias, clazz);
					}
				} catch (ClassNotFoundException e) {
					throw new BuilderException("Error registering typeAlias for '" + alias + "'. Cause: " + e, e);
				}
			}
		}
	}

}
