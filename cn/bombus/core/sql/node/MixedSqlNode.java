package cn.bombus.core.sql.node;

import java.util.List;

import cn.bombus.core.sql.DynamicContext;

public class MixedSqlNode implements SqlNode {
	private List<SqlNode> contents;

	public MixedSqlNode(List<SqlNode> contents) {
		this.contents = contents;
	}

	public boolean apply(DynamicContext context) {
		for (SqlNode sqlNode : contents) {
			sqlNode.apply(context);
		}
		return true;
	}
}
