package reducerProg;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Reducer {
	
	private String localKey;
	private String[] filesToRead;
	private HashMap<String,ArrayList<Integer>> mapOutputs = new HashMap<String,ArrayList<Integer>>();
	private HashMap<String,Integer> reduceOutputs = new HashMap<String,Integer>();
	
	
	public Reducer(String key, String[] mapperIds)
	{
		localKey = key;
		filesToRead = mapperIds;
	}
	
	
	public void shuffle() throws FileNotFoundException
	{
		
		for(String fileID : filesToRead)
		{
			String path = "/cal/homes/mkubryk/workspace/shavadoop/tempData/UM-"+fileID+".txt";
			
			File file = new File(path);
			Scanner sc = new Scanner(file);   

	        while (sc.hasNextLine()) 
	        {
	            String line = sc.nextLine();
	            
	            StringTokenizer itr = new StringTokenizer(line);
	            String key = itr.nextToken();
	            
	    
	            if(key.equals(localKey))
	            {
	            	while(itr.hasMoreTokens())
					{	
						int value = Integer.parseInt(itr.nextToken());
						addToList(mapOutputs,key,value);
					}
	            }
				
	        }
	        sc.close();

		}
		
	}
	
	
	
	public void reduce()
	{
		reduceOutputs.put(localKey, 0);
		
		for(int i : mapOutputs.get(localKey))
		{
			reduceOutputs.put(localKey, reduceOutputs.get(localKey)+i);
		}
		
		// send the results of reducer to master program
		System.out.println(localKey+" "+reduceOutputs.get(localKey));
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
