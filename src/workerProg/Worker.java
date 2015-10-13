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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Worker {
	
	private Integer task_id;
	private String worker_task;
	private String filePath;
	
	// unsorted map
	//private HashMap<String,Integer> UM = new HashMap<String,Integer>();
	private HashMap<String,ArrayList<Integer>> UM = new HashMap<String,ArrayList<Integer>>();
	
	
	public Worker(String filePath, String id)
	{
		this.filePath = filePath;
		this.task_id = Integer.parseInt(id);
	}
	
	
	
	public void map()
	{
		/*
		 * Map function : produces the unsorted map
		 */
		
		
		File file = new File(filePath+task_id);
	    
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
					
					addToList(UM,key,1);
				}
				
				this.writeUM();
	        }
	        
	        sc.close();
	        
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	
	public void sendKeys() throws IOException
	{
		for(String s : UM.keySet())
		{
			ProcessBuilder pb = new ProcessBuilder("echo", s).inheritIO();
			pb.start() ;
			
			ProcessBuilder pb = new ProcessBuilder("echo", (Str task_id).inheritIO();
			pb.start() ;
		}
	}
	
	
	
	public void writeUM() throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter("/cal/homes/mkubryk/workspace/shavadoop/tempData/UM-"+task_id+".txt", "UTF-8");
		
		for(String k : UM.keySet())
		{
			writer.print(k);
			
			for(int i : UM.get(k))
			{
				writer.print(" "+i);
			}
			
			writer.println();
		}
		writer.close();
		
	}
	
	public void showUM()
	{
		ProcessBuilder pb = new ProcessBuilder("hostname").inheritIO();
		
		try 
		{
			pb.start();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		System.out.println("show :" + UM);
	}
	
	
	
	public void addToList( HashMap<String,ArrayList<Integer>> Hash, String mapKey, Integer myItem) {
		
	    ArrayList<Integer> itemsList = Hash.get(mapKey);

	    // if list does not exist create it
	    if(itemsList == null) itemsList = new ArrayList<Integer>();
	     
	    itemsList.add(myItem);
	    
	    Hash.put(mapKey, itemsList);
	    
	}
	
	
	
}
