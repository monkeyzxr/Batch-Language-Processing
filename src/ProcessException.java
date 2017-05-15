@SuppressWarnings("serial")
public class ProcessException extends Exception
{
	public ProcessException(String str)
	{
		super(str);
	}
	
	public ProcessException(String str, Throwable throwable)
	{
		super(str, throwable);
	}
}
