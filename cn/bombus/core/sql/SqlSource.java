package cn.bombus.core.sql;

public interface SqlSource {

  BoundSql getBoundSql(Object parameterObject);

}
