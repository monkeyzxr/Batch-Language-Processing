import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.w3c.dom.Element;

public class CmdCommand extends Command
{
	public String id, path, inPath, outPath, args;
	
	public CmdCommand(Element elem, Map<String,String> realFileNames) throws ProcessException 
	{
		String in = elem.getAttribute("in");
        String out = elem.getAttribute("out");
            
        //redirect input from file:path to get the real file name
        this.inPath = realFileNames.get(in);
    	    	
    	//redirect output from file:path to get the real file name
    	this.outPath = realFileNames.get(out);
    	    	
    	if(out == null || out.isEmpty() || outPath == null || outPath.isEmpty())
    	{
            throw new ProcessException("Unable to locate 'Out' FileCommand with id " + out);
    	}
	}
		
	@Override
	public void describe()
	{
		System.out.println("Parsing cmd");	
 	}
	
	@Override
	public void execute(Batch batch) throws ProcessException
	{
		String workingDir = batch.getWorkingDir();
		ProcessBuilder processBuilder = new ProcessBuilder();
		
		List<String> command = new ArrayList<String>();		
		command.add(path);
		
		if(!(args == null || args.isEmpty()))//use sample code
		{
			StringTokenizer st = new StringTokenizer(args);
			while(st.hasMoreTokens())
			{
				String tok = st.nextToken();
				command.add(tok);
			}
		}
		
		processBuilder.directory(new File(workingDir));
		
		if(!(inPath == null || inPath.isEmpty()))
		{
			if(inPath != null)
			{
				File inFile = new File(workingDir,inPath);
				processBuilder.redirectInput(inFile);
			}
			else 
			{
				throw new ProcessException("Unable to locate input file");
			}	
		}
		
		if(!(outPath == null || outPath.isEmpty()))
		{
			if(outPath != null)
			{
				File outFile = new File(workingDir,outPath);
				processBuilder.redirectOutput(outFile);
			}
			else 
			{
				throw new ProcessException("Unable to locate output file");
			}	
		}
		
					
		System.out.println("Command: " + this.id);
		processBuilder.command(command);
		
		try 
		{
			Process process = processBuilder.start();
			System.out.println("Waiting for " + this.id + " to exit");
			process.waitFor();
			process.destroy();
			System.out.println(this.id + " has exited");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (InterruptedException e2) 
		{
			e2.printStackTrace();
		}
	}
	
	public void parse(Element element) throws ProcessException
	{
		this.id = element.getAttribute("id");
		if(id == null || id.isEmpty())
		{
			throw new ProcessException("Missing ID in CMD command");
		}
		
		this.path = element.getAttribute("path");
		
		if(path == null || path.isEmpty())
		{
			throw new ProcessException("Missing Path in CMD command");
		}

		this.args = element.getAttribute("args");
	}
	
	@Override
	public String getId()
	{
		return id;
	}	
}
