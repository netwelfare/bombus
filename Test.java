import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bombus.core.Configuration;
import cn.bombus.core.MappedStatement;
import cn.bombus.core.XMLMapperBuilder;
import cn.bombus.core.sql.BoundSql;
import cn.bombus.core.sql.SqlSource;
import entity.QueryStatisUserGroup;

public class Test
{
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, IOException
	{
		String file = "conf/olap.xml";
		InputStream inputStream = ClassLoader.getSystemResourceAsStream(file);
		Configuration configuration = new Configuration();
		XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, file,
				configuration.getSqlFragments());
		mapperParser.parse();
		MappedStatement statement = configuration.getMappedStatement("pay_user_num", false);
		SqlSource sql = statement.getSqlSource();
		QueryStatisUserGroup group = new QueryStatisUserGroup();
		group.setStatFrequency("HOUR");
		group.setStartDate(new Date());
		group.setEndDate(new Date());
		List<String> pageNames = new ArrayList<String>();
		pageNames.add("gold");
		pageNames.add("yyg");
		group.setPageNames(pageNames);

		Map<String, String> map = new HashMap<String, String>();
		map.put("groupby", "test");
		BoundSql sql2 = sql.getBoundSql(map);
		System.out.println((sql2.getSql()));
		inputStream.close();
	}

}
