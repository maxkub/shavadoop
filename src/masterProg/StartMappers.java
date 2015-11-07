package masterProg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class StartMappers extends StartWorkers implements Runnable
{
	
	private String workerIP;
	private String filePath;
	//private HashMap<String,Integer> result;
	
	public StartMappers(String workerIP, String filePath)
	{
		this.filePath = filePath;
		this.workerIP = workerIP;
	}
	
	
	
	public void run()
	{
		
		//ProcessBuilder pb = new ProcessBuilder("ssh", workerIP, 
		//		"java -jar ~/shavadoopMapper.jar "+filePath);//.inheritIO();
		
		ProcessBuilder pb = new ProcessBuilder("ssh", workerIP, 
				"java -jar ~/shavadoopWorker.jar map "+filePath);//.inheritIO();
				
		try 
		{
			Process p = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			result = listenToWorkers(br);
			//System.out.println("startmappers result "+result);
		    
					
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
			
			hash.put(key, task);
        }
        
        return hash;
	
	}
    */
	

}
