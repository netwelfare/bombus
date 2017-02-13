package cn.bombus.core.sql.node;

import cn.bombus.core.sql.DynamicContext;

public interface SqlNode {
  boolean apply(DynamicContext context);
}
