package mapperProg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Mapper {
	
	
	private Integer task_id;
	private String filePath;
	
	private HashMap<String,ArrayList<Integer>> UM = new HashMap<String,ArrayList<Integer>>();
	
	
	public Mapper(String filePath, String id)
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
	        	        
	        sendKeys();
	        
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
			System.out.println(s+" "+task_id);
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
	
	
	
	public void addToList( HashMap<String,ArrayList<Integer>> Hash, String mapKey, Integer myItem) 
	{
		
	    ArrayList<Integer> itemsList = Hash.get(mapKey);

	    // if list does not exist create it
	    if(itemsList == null) itemsList = new ArrayList<Integer>();
	     
	    itemsList.add(myItem);
	    
	    Hash.put(mapKey, itemsList);
	    
	}
	

}
