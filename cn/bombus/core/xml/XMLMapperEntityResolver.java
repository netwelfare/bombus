package cn.bombus.core.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cn.bombus.core.Resources;

public class XMLMapperEntityResolver implements EntityResolver
{

	private static final Map<String, String> doctypeMap = new HashMap<String, String>();
	private static final String MYBATIS_MAPPER_DOCTYPE = "-//bombus.cn//DTD Mapper 3.0//EN".toUpperCase(Locale.ENGLISH);
	private static final String MYBATIS_MAPPER_URL = "http://bombus.cn/dtd/mapper.dtd".toUpperCase(Locale.ENGLISH);
	private static final String IBATIS_MAPPER_DTD = "cn/bombus/core/xml/dtd/mapper.dtd";
	static
	{
		doctypeMap.put(MYBATIS_MAPPER_URL, IBATIS_MAPPER_DTD);
		doctypeMap.put(MYBATIS_MAPPER_DOCTYPE, IBATIS_MAPPER_DTD);
	}

	public InputSource resolveEntity(String publicId, String systemId) throws SAXException
	{
		if (publicId != null)
		{
			publicId = publicId.toUpperCase(Locale.ENGLISH);
		}
		if (systemId != null)
		{
			systemId = systemId.toUpperCase(Locale.ENGLISH);
		}
		InputSource source = null;
		try
		{
			String path = doctypeMap.get(publicId);
			source = getInputSource(path, source);
			if (source == null)
			{
				path = doctypeMap.get(systemId);
				source = getInputSource(path, source);
			}
		}
		catch (Exception e)
		{
			throw new SAXException(e.toString());
		}
		return source;
	}

	private InputSource getInputSource(String path, InputSource source)
	{
		if (path != null)
		{
			InputStream in;
			try
			{
				in = Resources.getResourceAsStream(path);
				source = new InputSource(in);
			}
			catch (IOException e)
			{
				// ignore, null is ok
			}
		}
		return source;
	}

}