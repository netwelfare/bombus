package cn.bombus.core.exception;

public class IncompleteCacheException extends BuilderException
{
	private static final long serialVersionUID = -3697292286890900315L;

	public IncompleteCacheException()
	{
		super();
	}

	public IncompleteCacheException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public IncompleteCacheException(String message)
	{
		super(message);
	}

	public IncompleteCacheException(Throwable cause)
	{
		super(cause);
	}
}
