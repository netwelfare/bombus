package cn.bombus.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * A class to simplify access to resources through the classloader.
 */
public class Resources
{
	private static ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();

	/**
	 * Charset to use when calling getResourceAsReader. null means use the
	 * system default.
	 */

	private static Charset charset;

	Resources()
	{
	}

	/**
	 * Returns the default classloader (may be null).
	 */
	public static ClassLoader getDefaultClassLoader()
	{
		return classLoaderWrapper.defaultClassLoader;
	}

	/**
	 * Sets the default classloader
	 */
	public static void setDefaultClassLoader(ClassLoader defaultClassLoader)
	{
		classLoaderWrapper.defaultClassLoader = defaultClassLoader;
	}

	/**
	 * Returns the URL of the resource on the classpath
	 */
	public static URL getResourceURL(String resource) throws IOException
	{
		return classLoaderWrapper.getResourceAsURL(resource);
	}

	/**
	 * Returns the URL of the resource on the classpath
	 */
	public static URL getResourceURL(ClassLoader loader, String resource) throws IOException
	{
		URL url = classLoaderWrapper.getResourceAsURL(resource, loader);
		if (url == null)
			throw new IOException("Could not find resource " + resource);
		return url;
	}

	/**
	 * Returns a resource on the classpath as a Stream object
	 */
	public static InputStream getResourceAsStream(String resource) throws IOException
	{
		return getResourceAsStream(null, resource);
	}

	/**
	 * Returns a resource on the classpath as a Stream object
	 */
	public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException
	{
		InputStream in = classLoaderWrapper.getResourceAsStream(resource, loader);
		if (in == null)
			throw new IOException("Could not find resource " + resource);
		return in;
	}

	/**
	 * Returns a resource on the classpath as a Properties object
	 */
	public static Properties getResourceAsProperties(String resource) throws IOException
	{
		Properties props = new Properties();
		InputStream in = getResourceAsStream(resource);
		props.load(in);
		in.close();
		return props;
	}

	/**
	 * Returns a resource on the classpath as a Properties object
	 */
	public static Properties getResourceAsProperties(ClassLoader loader, String resource) throws IOException
	{
		Properties props = new Properties();
		InputStream in = getResourceAsStream(loader, resource);
		props.load(in);
		in.close();
		return props;
	}

	/**
	 * Returns a resource on the classpath as a Reader object
	 */
	// reader其实就是InputStreamReader，加载资
	public static Reader getResourceAsReader(String resource) throws IOException
	{
		Reader reader;
		if (charset == null)
		{
			reader = new InputStreamReader(getResourceAsStream(resource));
		}
		else
		{
			reader = new InputStreamReader(getResourceAsStream(resource), charset);
		}
		return reader;
	}

	/**
	 * Returns a resource on the classpath as a Reader object
	 */
	public static Reader getResourceAsReader(ClassLoader loader, String resource) throws IOException
	{
		Reader reader;
		if (charset == null)
		{
			reader = new InputStreamReader(getResourceAsStream(loader, resource));
		}
		else
		{
			reader = new InputStreamReader(getResourceAsStream(loader, resource), charset);
		}
		return reader;
	}

	/**
	 * Returns a resource on the classpath as a File object
	 */
	public static File getResourceAsFile(String resource) throws IOException
	{
		return new File(getResourceURL(resource).getFile());
	}

	/**
	 * Returns a resource on the classpath as a File object
	 */
	public static File getResourceAsFile(ClassLoader loader, String resource) throws IOException
	{
		return new File(getResourceURL(loader, resource).getFile());
	}

	public static InputStream getUrlAsStream(String urlString) throws IOException
	{
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		return conn.getInputStream();
	}

	public static Reader getUrlAsReader(String urlString) throws IOException
	{
		Reader reader;
		if (charset == null)
		{
			reader = new InputStreamReader(getUrlAsStream(urlString));
		}
		else
		{
			reader = new InputStreamReader(getUrlAsStream(urlString), charset);
		}
		return reader;
	}

	public static Properties getUrlAsProperties(String urlString) throws IOException
	{
		Properties props = new Properties();
		InputStream in = getUrlAsStream(urlString);
		props.load(in);
		in.close();
		return props;
	}

	public static Class<?> classForName(String className) throws ClassNotFoundException
	{
		return classLoaderWrapper.classForName(className);
	}

	public static Charset getCharset()
	{
		return charset;
	}

	public static void setCharset(Charset charset)
	{
		Resources.charset = charset;
	}

}
