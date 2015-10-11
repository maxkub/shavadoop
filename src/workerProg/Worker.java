package workerProg;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Worker {
	
	private String worker_id;
	private String worker_task;
	private String filePath;
	
	// unsorted map
	private HashMap<String,Integer> UM = new HashMap<String,Integer>();
	
	public Worker()
	{
	}
	
	public Worker(String filePath)
	{
		this.filePath = filePath; /// tester que filePath est vraie une copie
	}
	
	public void setFile(String filePath)
	{
		this.filePath = filePath; /// tester que filePath est vraie une copie
	}
	
	public void map(int l)
	{
		/*
		 * Map function : produces the unsorted map
		 */
		Path path = Paths.get(filePath);
		try 
		{
			String line = Files.readAllLines(path,StandardCharsets.UTF_8).get(l);
			
			StringTokenizer itr = new StringTokenizer(line);
			while(itr.hasMoreTokens())
			{	
				UM.put(itr.nextToken(), 1);	
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void printUM()
	{
		ProcessBuilder pb = new ProcessBuilder("hostname").inheritIO();
		
		try {
			Process p = pb.start();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(UM);
	}
}
