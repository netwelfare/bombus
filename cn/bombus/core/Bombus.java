package cn.bombus.core;

import java.io.IOException;
import java.io.InputStream;

import cn.bombus.core.sql.BoundSql;
import cn.bombus.core.sql.SqlSource;

public class Bombus {

	private String file;
    private InputStream inputStream;
	public Bombus(String file) {
		this.file=file;
		ClassLoaderWrapper loader =new ClassLoaderWrapper();
		inputStream = loader.getResourceAsStream(file);
	}

	public String getSQL(String id, Object parameterObject){
		Configuration configuration = new Configuration();
		XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, file,
				configuration.getSqlFragments());
		mapperParser.parse();
		MappedStatement statement = configuration.getMappedStatement(id, false);
		SqlSource sqlSrc = statement.getSqlSource();
		BoundSql boundSql = sqlSrc.getBoundSql(parameterObject);
		return boundSql.getSql();
	}
	
	public void close(){
		if(inputStream!=null){
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
