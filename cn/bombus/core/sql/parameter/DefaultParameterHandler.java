package cn.bombus.core.sql.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.bombus.core.Configuration;
import cn.bombus.core.MappedStatement;
import cn.bombus.core.sql.BoundSql;
import cn.bombus.core.sql.type.TypeHandlerRegistry;

public class DefaultParameterHandler implements ParameterHandler {

	private final TypeHandlerRegistry typeHandlerRegistry;

	private final MappedStatement mappedStatement;
	private final Object parameterObject;
	private BoundSql boundSql;
	private Configuration configuration;

	public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
		this.mappedStatement = mappedStatement;
		this.configuration = mappedStatement.getConfiguration();
		this.typeHandlerRegistry = null;
		this.parameterObject = parameterObject;
		this.boundSql = boundSql;
	}

	public Object getParameterObject() {
		return parameterObject;
	}

	public void setParameters(PreparedStatement ps) throws SQLException {
		// ErrorContext.instance().activity("setting
		// parameters").object(mappedStatement.getParameterMap().getId());
		// List<ParameterMapping> parameterMappings =
		// boundSql.getParameterMappings();
		// if (parameterMappings != null) {
		// MetaObject metaObject = parameterObject == null ? null :
		// configuration.newMetaObject(parameterObject);
		// for (int i = 0; i < parameterMappings.size(); i++) {
		// ParameterMapping parameterMapping = parameterMappings.get(i);
		// if (parameterMapping.getMode() != ParameterMode.OUT) {
		// Object value;
		// String propertyName = parameterMapping.getProperty();
		// PropertyTokenizer prop = new PropertyTokenizer(propertyName);
		// if (parameterObject == null) {
		// value = null;
		// } else if
		// (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
		// value = parameterObject;
		// } else if (boundSql.hasAdditionalParameter(propertyName)) {
		// value = boundSql.getAdditionalParameter(propertyName);
		// } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
		// && boundSql.hasAdditionalParameter(prop.getName())) {
		// value = boundSql.getAdditionalParameter(prop.getName());
		// if (value != null) {
		// value =
		// configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
		// }
		// } else {
		// value = metaObject == null ? null :
		// metaObject.getValue(propertyName);
		// }
		// TypeHandler typeHandler = parameterMapping.getTypeHandler();
		// if (typeHandler == null) {
		// throw new ExecutorException("There was no TypeHandler found for
		// parameter " + propertyName + " of statement " +
		// mappedStatement.getId());
		// }
		// typeHandler.setParameter(ps, i + 1, value,
		// parameterMapping.getJdbcType());
		// }
		// }
		// }
	}

}
