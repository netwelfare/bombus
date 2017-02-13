package cn.bombus.core.sql.key;

import java.sql.Statement;
import java.util.concurrent.Executor;

import cn.bombus.core.MappedStatement;

public class NoKeyGenerator implements KeyGenerator {

	public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
	}

	public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
	}

}
