package cn.bombus.core.exception;

public class IncompleteStatementException extends BuilderException
{
	private static final long serialVersionUID = 3329879670592089325L;

	public IncompleteStatementException()
	{
		super();
	}

	public IncompleteStatementException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public IncompleteStatementException(String message)
	{
		super(message);
	}

	public IncompleteStatementException(Throwable cause)
	{
		super(cause);
	}
}
