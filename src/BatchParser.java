import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BatchParser 
{
	private static List<Element> pipeElems = new ArrayList<Element>();
	private static Map<String,String> realFileNames = new HashMap<String,String>();
	
	public Batch buildBatch(File batchFile)
	{
		Batch batch = new Batch();
		try {
			FileInputStream fis = new FileInputStream(batchFile);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbuilder = dbf.newDocumentBuilder();
			Document document = dbuilder.parse(fis);
			Element element = document.getDocumentElement();
			NodeList nodes = element.getChildNodes();
			for(int i = 0; i < nodes.getLength(); i++)
			{
				Node node = nodes.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element elem = (Element) node;
					String cmdName = elem.getNodeName();
					Command command = buildCommand(elem);
					if(cmdName.equalsIgnoreCase("wd"))
					{
						WdCommand wdCommand = (WdCommand)command;
						batch.setWorkingDir(wdCommand.getPath());
					}
					batch.addCommand(command);
				}
			}				
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return batch;
	}
		
	public Command buildCommand(Element elem) throws ProcessException
	{
		Command command = null;
		String nodeName = elem.getNodeName();
		
		if(nodeName == null)
		{
			throw new ProcessException("One element of the batch file is null!");
		}
		else if(nodeName.equalsIgnoreCase("wd"))
		{
			command = new WdCommand();			
		}
		else if(nodeName.equalsIgnoreCase("file"))
		{
			command = new FileCommand(elem);
			realFileNames.put(command.getId(), ((FileCommand)command).getPath());
		}
		else if(nodeName.equalsIgnoreCase("cmd"))
		{
			command = new CmdCommand(elem, realFileNames);
		}
		else if(nodeName.equalsIgnoreCase("pipe"))
		{
			 NodeList pipeCmdNodes = elem.getChildNodes();
	            for(int idx = 0; idx < pipeCmdNodes.getLength(); idx++)
	            {
	                Node pipeChildNode = pipeCmdNodes.item(idx);
	                if(pipeChildNode.getNodeType() == Node.ELEMENT_NODE)
	                {
	                    pipeElems.add((Element)pipeChildNode);
	                }
	            }                     
	            command = new PipeCommand(pipeElems,realFileNames);
	   	}
		else 
		{
			throw new ProcessException("An unknown element in the batch file!");
		}
				
		command.describe();
		command.parse(elem);
						
		return command;
	}
	
	public static void putFiles(String file, String realName)
	{
    	realFileNames.put(file, realName);
    }
}
