import org.w3c.dom.Element;

public class WdCommand extends Command
{
	private String id;
	private String path;
	
	@Override
	public void describe()
	{
		System.out.println("Parsing wd");
	}

	@Override
	public void execute(Batch batch)
	{
		System.out.println("The working directory will be set to " + path);
	}
	
	@Override
	public void parse(Element element) throws ProcessException
	{
		this.id = element.getAttribute("id");
		if(id == null || id.isEmpty()){
			throw new ProcessException("Missing Id in wd command");
		}
		
		this.path = element.getAttribute("path");
		if(path == null || path.isEmpty()){
			throw new ProcessException("Missing Path in wd command");
		}	
	}
	
	public String getPath()
	{
		return path;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
}
