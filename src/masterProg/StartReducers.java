package masterProg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class StartReducers extends StartWorkers implements Runnable
{
	
	
	private String key;
	private String workerIP;
	private String tempfiles;
	//private HashMap<String,Integer> result;
	
	public StartReducers(String key, String workerIP, String tempfiles)
	{
		this.key      = key;
		this.tempfiles = tempfiles;
		this.workerIP = workerIP;
	}
	
	
	public void run()
	{

		//ProcessBuilder pb = new ProcessBuilder("ssh", workerIP, 
		//		"java -jar ~/shavadoopReducer.jar "+key+" "+ tempfiles);//.inheritIO();
		
		ProcessBuilder pb = new ProcessBuilder("ssh", workerIP, 
				"java -jar ~/shavadoopWorker.jar reduce "+key+" "+ tempfiles);//.inheritIO();
		
		Process p;
		try 
		{
			p = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			result = listenToWorkers(br);
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	/*
	
	public HashMap<String,Integer> getValue()
	{
		return result;
	}
	
	
	
	
	private HashMap<String,Integer> listenToWorkers(BufferedReader br) throws IOException
	{
		
		
		HashMap<String,Integer> hash = new HashMap<String,Integer>();
		
		String s;
        while ((s = br.readLine()) != null)
        {
        	StringTokenizer itr = new StringTokenizer(s.toString());
							
        	String key = itr.nextToken();
			int task = Integer.parseInt(itr.nextToken());
			
			hash.put(key,task);
			
			//System.out.println("master listen : "+task+" "+key);
				
			//hash.addToList(key,task);

        }
        
        return hash;
	
	}
    */
	


}
