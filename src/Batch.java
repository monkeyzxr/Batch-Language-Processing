import java.util.List;
import java.util.ArrayList;

public class Batch 
{
	private String workingDir;
	private List<Command> commands = new ArrayList<Command>();
		
	public void setWorkingDir(String workingDir)
	{
		this.workingDir = workingDir;
	}
	
	public String getWorkingDir()
	{
		return workingDir;
	}
	
	public List<Command> getCommands()
	{
		return commands;
	}
	
	public void addCommand(Command command)
	{
		commands.add(command);
	}
}
