import org.w3c.dom.Element;

public abstract class Command 
{	
	public abstract void describe();
	
	public abstract void execute(Batch batch) throws ProcessException;
	
	public abstract void parse(Element elem) throws ProcessException;
	
	public abstract String getId();
}
