package cn.bombus.core.sql.node;

import cn.bombus.core.Configuration;

public class WhereSqlNode extends TrimSqlNode
{

	public WhereSqlNode(Configuration configuration, SqlNode contents)
	{
		super(configuration, contents, "WHERE", "AND |OR ", null, null);
	}

}
