package cn.bombus.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cn.bombus.core.reflect.DefaultObjectFactory;
import cn.bombus.core.reflect.MetaObject;
import cn.bombus.core.reflect.ObjectFactory;
import cn.bombus.core.sql.parameter.ParameterMap;
import cn.bombus.core.sql.result.ResultMap;
import cn.bombus.core.sql.type.TypeAliasRegistry;
import cn.bombus.core.sql.type.TypeHandlerRegistry;
import cn.bombus.core.sql.wrap.DefaultObjectWrapperFactory;
import cn.bombus.core.sql.wrap.ObjectWrapperFactory;
import cn.bombus.core.xml.XNode;

public class Configuration {
	protected Properties variables = new Properties();
	protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
	protected ObjectFactory objectFactory = new DefaultObjectFactory();
	protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
	protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
	protected final Map<String, ResultMap> resultMaps = new StrictMap<String, ResultMap>("Result Maps collection");
	protected final Map<String, XNode> sqlFragments = new StrictMap<String, XNode>(
			"XML fragments parsed from previous mappers");
	protected final Map<String, MappedStatement> mappedStatements = new StrictMap<String, MappedStatement>(
			"Mapped Statements collection");

	// protected final Map<String, KeyGenerator> keyGenerators = new
	// StrictMap<String, KeyGenerator>(
	// "Key Generators collection");
	// protected boolean useGeneratedKeys = false;
	// protected final Collection<XMLStatementBuilder> incompleteStatements =
	// new LinkedList<XMLStatementBuilder>();
	protected final Map<String, ParameterMap> parameterMaps = new StrictMap<String, ParameterMap>(
			"Parameter Maps collection");

	public Configuration() {

	}

	public Properties getVariables() {
		return variables;
	}

	public TypeAliasRegistry getTypeAliasRegistry() {
		return typeAliasRegistry;
	}

	public MetaObject newMetaObject(Object object) {
		return MetaObject.forObject(object, objectFactory, objectWrapperFactory);
	}

	// public TypeHandlerRegistry getTypeHandlerRegistry()
	// {
	// return typeHandlerRegistry;
	// }

	public ResultMap getResultMap(String id) {
		return resultMaps.get(id);
	}

	protected static class StrictMap<J extends String, K extends Object> extends HashMap<J, K> {

		private String name;

		public StrictMap(String name, int initialCapacity, float loadFactor) {
			super(initialCapacity, loadFactor);
			this.name = name;
		}

		public StrictMap(String name, int initialCapacity) {
			super(initialCapacity);
			this.name = name;
		}

		public StrictMap(String name) {
			super();
			this.name = name;
		}

		public StrictMap(String name, Map<? extends J, ? extends K> m) {
			super(m);
			this.name = name;
		}

		public K put(J key, K value) {
			if (containsKey(key))
				throw new IllegalArgumentException(name + " already contains value for " + key);
			if (key.contains(".")) {
				final String shortKey = getShortName(key);
				if (super.get(shortKey) == null) {
					super.put((J) shortKey, value);
				} else {
					super.put((J) shortKey, (K) new Ambiguity(shortKey));
				}
			}
			return super.put(key, value);
		}

		public K get(Object key) {
			K value = super.get(key);
			if (value == null) {
				throw new IllegalArgumentException(name + " does not contain value for " + key);
			}
			if (value instanceof Ambiguity) {
				throw new IllegalArgumentException(((Ambiguity) value).getSubject() + " is ambiguous in " + name
						+ " (try using the full name including the namespace, or rename one of the entries)");
			}
			return value;
		}

		private String getShortName(J key) {
			final String[] keyparts = key.split("\\.");
			final String shortKey = keyparts[keyparts.length - 1];
			return shortKey;
		}

		protected static class Ambiguity {
			private String subject;

			public Ambiguity(String subject) {
				this.subject = subject;
			}

			public String getSubject() {
				return subject;
			}
		}
	}

	public Map<String, XNode> getSqlFragments() {
		return sqlFragments;
	}

	public MappedStatement getMappedStatement(String id) {
		return this.getMappedStatement(id, true);
	}

	public MappedStatement getMappedStatement(String id, boolean validateIncompleteStatements) {

		return mappedStatements.get(id);
	}

	// public boolean hasKeyGenerator(String id)
	// {
	// return keyGenerators.containsKey(id);
	// }
	//
	// public KeyGenerator getKeyGenerator(String id)
	// {
	// return keyGenerators.get(id);
	// }
	//
	// public boolean isUseGeneratedKeys()
	// {
	// return useGeneratedKeys;
	// }
	//
	// public void addKeyGenerator(String id, KeyGenerator keyGenerator)
	// {
	// keyGenerators.put(id, keyGenerator);
	// }
	//
	// public Collection<XMLStatementBuilder> getIncompleteStatements()
	// {
	// return incompleteStatements;
	// }

	public void addResultMap(ResultMap rm) {
		resultMaps.put(rm.getId(), rm);
	}

	public void addMappedStatement(MappedStatement ms) {
		mappedStatements.put(ms.getId(), ms);
	}

	public ParameterMap getParameterMap(String id) {
		return parameterMaps.get(id);
	}

	public void addParameterMap(ParameterMap pm) {
		parameterMaps.put(pm.getId(), pm);
	}

	public TypeHandlerRegistry getTypeHandlerRegistry() {
		return typeHandlerRegistry;
	}
}
