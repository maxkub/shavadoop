package workerProg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
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
			
			System.out.println("line "+l+" "+line);
			
			StringTokenizer itr = new StringTokenizer(line);
			while(itr.hasMoreTokens())
			{	
				String key = itr.nextToken();
				
				if(UM.containsKey(key))
				{
					UM.put(key, UM.get(key)+1);	
				}
				else
				{
					UM.put(key, 1);	
				}
				
				System.out.println("um : "+l+" "+key+" "+UM.get(key));
				
			}
			
			this.writeUM(l);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		
	}
	
	
	
	
	public void map2(int l)
	{
		/*
		 * Map function : produces the unsorted map
		 */
		
		
		File file = new File(filePath+l);
	    
	    try 
	    {
	        Scanner sc = new Scanner(file);   

	        while (sc.hasNextLine()) 
	        {
	            String line = sc.nextLine();
	            
	            StringTokenizer itr = new StringTokenizer(line);
				while(itr.hasMoreTokens())
				{	
					String key = itr.nextToken();
					
					if(UM.containsKey(key))
					{
						UM.put(key, UM.get(key)+1);	
					}
					else
					{
						UM.put(key, 1);	
					}
					
					System.out.println("um : "+l+" "+key+" "+UM.get(key));
					
				}
				
				
				this.writeUM(l);
	       
	        }
	        
	        sc.close();
	        
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	
	
	public void writeUM(int l) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter("/cal/homes/mkubryk/workspace/shavadoop/tempData/UM-"+l+".txt", "UTF-8");
		
		for(String k : UM.keySet())
		{
			System.out.println("UM : "+k+" "+UM.get(k));
			writer.println(k+" "+UM.get(k));
		}
		writer.close();
		
	}
	
	public void showUM()
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
