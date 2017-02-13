package cn.bombus.core.sql.key;

import java.sql.Statement;
import java.util.concurrent.Executor;

import cn.bombus.core.MappedStatement;

public interface KeyGenerator {

	void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter);

	void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter);

}
