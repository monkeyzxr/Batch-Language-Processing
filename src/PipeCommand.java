import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.w3c.dom.Element;

public class PipeCommand extends Command
{
	String id1, id2, realIn, realOut;
	private List<String> command1 = new ArrayList<String>();
    private List<String> command2 = new ArrayList<String>();
	
	public PipeCommand(List<Element> pipeElems , Map<String, String> realFileNames) throws ProcessException 
	{
        Element pipeIn = pipeElems.get(0);
        Element pipeOut = pipeElems.get(1);
        
        String id1 = pipeIn.getAttribute("id");
        if (id1 == null || id1.isEmpty()) {
            throw new ProcessException("Missing Id in first CMD Command");
        }
        
        String path1 = pipeIn.getAttribute("path");
        if (path1 == null || path1.isEmpty()) {
            throw new ProcessException("Missing Path in first CMD Command");
        }
        
        this.id1 = id1;
        
        command1.add(path1);
        String arg = pipeIn.getAttribute("args");
        
        if (!(arg == null || arg.isEmpty())) {
            StringTokenizer st = new StringTokenizer(arg);
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                command1.add(tok);
            }
        }
        
        String in = pipeIn.getAttribute("in");
        this.realIn = realFileNames.get(in);
        
        String id2 = pipeOut.getAttribute("id");
        if (id2 == null || id2.isEmpty()) {
            throw new ProcessException("Missing Id in second CMD Command");
        }
        
        String path2 = pipeOut.getAttribute("path");
        if (path2 == null || path2.isEmpty()) {
            throw new ProcessException("Missing Path in second CMD Command");
        }
        
        this.id2 = id2;
        
        command2.add(path2);
        
        String arg2 = pipeOut.getAttribute("args");
        
        if (!(arg2 == null || arg2.isEmpty())) {
            StringTokenizer st = new StringTokenizer(arg2);
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                command2.add(tok);
            }
        }
        
        String out = pipeOut.getAttribute("out");
        this.realOut = realFileNames.get(out);
    }
	
	public void describe()
	{
		System.out.println("Parsing Pipe");
	}
	
	public void execute(Batch batch) throws ProcessException
	{
		try 
		{
			String workingDir = batch.getWorkingDir();

			ProcessBuilder inBuilder = new ProcessBuilder(command1);
			
			System.out.println("Command: " + id1);
			System.out.println("Waiting for cmd1 to exit");
			inBuilder.directory(new File(workingDir));
			File wd = inBuilder.directory();
			final Process inProcess = inBuilder.start();
			FileInputStream fis = new FileInputStream(new File(wd, realIn));
			OutputStream os = inProcess.getOutputStream();
			
			int aChar;
			while((aChar = fis.read()) != -1)
			{
				os.write(aChar);
			}
			os.close();
			fis.close();
			System.out.println(id1 + " has exited");
			
			ProcessBuilder outBuilder = new ProcessBuilder(command2);
			
			System.out.println("Command: " + id2);
			System.out.println("Waiting for cmd2 to exit");
			outBuilder.directory(wd);
			final Process outProcess = outBuilder.start();
			InputStream inout = inProcess.getInputStream();
			OutputStream outin = outProcess.getOutputStream();
			
			copyStreams(inout, outin);

			File outFile = new File(wd, realOut);
			FileOutputStream fos = new FileOutputStream(outFile);
			InputStream is = outProcess.getInputStream();
			
			while((aChar = is.read()) != -1)
			{
				fos.write(aChar);
			}
			fos.close();
			System.out.println(id2 + " has exited");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
	}
	
	public void parse(Element element) throws ProcessException
	{
		//System.out.println("Parsing pipe");	
	}
	
	public static void copyStreams(final InputStream is, final OutputStream os) 
	{
		Runnable copyThread = (new Runnable() 
		{
			@Override
			public void run()
			{
				try 
				{
					int achar;
					while ((achar = is.read()) != -1) 
					{
						os.write(achar);
					}
					os.close();
				}
				catch (IOException ex) 
				{
					throw new RuntimeException(ex.getMessage(), ex);
				}
			}
		});
		new Thread(copyThread).start();
	}

	@Override
	public String getId() 
	{
		return null;
	}
}
