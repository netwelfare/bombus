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
		String file = "conf/DataStatisUserGroup.xml";
		InputStream inputStream = ClassLoader.getSystemResourceAsStream(file);
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

		Map<String, String> map = new HashMap<String, String>();
		map.put("statFrequency", "HOUR");
		map.put("hour", "where 1=1 and 'a'");
		map.put("name", "wxf");
		BoundSql sql2 = sql.getBoundSql(group);
		System.out.println((sql2.getSql()));
		inputStream.close();

		//	    List<String> list = new ArrayList<String>();
		//	    list.add("wxf");
		//	    list.add("netease");
		//	    
		//	    String [] array =new String[2];
		//	    list.toArray(array);
		//	    System.out.println(array);
		//	    
		//	    System.out.println(List.class.getSimpleName());
		//	    
		//	    Class c = Integer.class;
		//	    Constructor con=  c.getDeclaredConstructor(String.class);
		//	    con.setAccessible(false);
		//	    Object o=  con.newInstance("10");
		//	    System.out.println(o);

	}

}
