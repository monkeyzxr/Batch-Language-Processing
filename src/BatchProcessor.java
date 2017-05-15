import java.io.File;
import java.util.List;
import java.io.IOException;


public class BatchProcessor 
{
	public static void main(String[] args) throws IOException
	{
		String fileName = null;
		if (args.length == 1 )
		{
			fileName = args[0];
		} 
		else 
		{
			System.out.println("File NOT Assigned !!!");
			System.out.println("Program Terminated !");
			return;
		}
		File file = new File(fileName);
		System.out.println("Creating a batch from " + fileName);
		BatchParser batchParser = new BatchParser();
		Batch batch = batchParser.buildBatch(file);
		BatchProcessor batchProcessor = new BatchProcessor();
		batchProcessor.executeBatch(batch);
		
	}
	
	public void executeBatch(Batch batch)
	{
		List<Command> commands = batch.getCommands();
		for (int i = 0; i < commands.size(); i++)
		{
			try 
			{
				commands.get(i).execute(batch);
			} 
			catch (ProcessException e) 
			{
				e.printStackTrace();
			}	
		}
		System.out.println("Batch Finished!");
	}
}
