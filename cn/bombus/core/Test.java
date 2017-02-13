package cn.bombus.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bombus.core.sql.BoundSql;
import cn.bombus.core.sql.SqlSource;
import entity.QueryStatisUserGroup;

public class Test
{
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, IOException
	{
		String file = "conf/DataStatisUserGroup.xml";
		ClassLoaderWrapper loader = new ClassLoaderWrapper();
		InputStream inputStream = loader.getResourceAsStream(file);
		Configuration configuration = new Configuration();
		XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, file,
				configuration.getSqlFragments());
		mapperParser.parse();
		MappedStatement statement = configuration.getMappedStatement("getStatisUserGroups", false);
		SqlSource sql = statement.getSqlSource();
		QueryStatisUserGroup group = new QueryStatisUserGroup();
		group.setStatFrequency("HOUR");
		group.setStartDate(new Date());
		group.setEndDate(new Date());
		List<String> pageNames = new ArrayList<String>();
		pageNames.add("gold");
		pageNames.add("yyg");
		group.setPageNames(pageNames);

		inputStream.close();
		Map<String, String> map = new HashMap<String, String>();
		map.put("statFrequency", "HOUR");
		map.put("hour", "'who'");
		map.put("wxf", "where 1=1 and a");
		BoundSql sql2 = sql.getBoundSql(map);
		System.out.println((sql2.getSql()));

	}

}
