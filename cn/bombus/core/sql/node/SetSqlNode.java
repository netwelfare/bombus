package cn.bombus.core.sql.node;

import cn.bombus.core.Configuration;

public class SetSqlNode extends TrimSqlNode {

	public SetSqlNode(Configuration configuration, SqlNode contents) {
		super(configuration, contents, "SET", null, null, ",");
	}
}
