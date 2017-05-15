import org.w3c.dom.Element;

public class FileCommand extends Command
{
	private String id, path;
	
	public FileCommand(Element elem) throws ProcessException 
	{
		String id = elem.getAttribute("id");
		if (id == null || id.isEmpty()) 
		{
			throw new ProcessException("Missing Id in File Command");
		}

		String path = elem.getAttribute("path");
		if (path == null || path.isEmpty()) 
		{
			throw new ProcessException("Missing Path in File Command");
		}
		
		this.id = id;
		this.path = path;
	}
	
	@Override
	public void describe(){}
	
	@Override
	public void execute(Batch batch)
	{
		System.out.println("File Command on file: " + path);
	}
	
	@Override
	public void parse(Element element) throws ProcessException
	{
		System.out.println("Parsing file");
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
	public String getPath()
	{
		return path;
	}
	
	
	
}
