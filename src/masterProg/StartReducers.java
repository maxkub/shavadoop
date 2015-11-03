package masterProg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class StartReducers implements Runnable
{
	
	
	private String key;
	private String workerIP;
	private String tempfiles;
	private Dico mapRedOutputs;
	
	public StartReducers(String key, String workerIP, String tempfiles, Dico mapRedOutputs)
	{
		this.mapRedOutputs = mapRedOutputs;
		this.key      = key;
		this.tempfiles = tempfiles;
		this.workerIP = workerIP;
	}
	
	
	public void run()
	{

		ProcessBuilder pb = new ProcessBuilder("ssh", workerIP, 
				"java -jar ~/shavadoopReducer.jar "+key+" "+ tempfiles);//.inheritIO();
		
		Process p;
		try 
		{
			p = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			synchronized(mapRedOutputs)
			{
				listenToWorkers(br, mapRedOutputs);
			}
			
			
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	private void listenToWorkers(BufferedReader br, Dico hash) throws IOException
	{
		/*
		 * listen to the workers to get their keys
		 */
		
		String s;
        while ((s = br.readLine()) != null)
        {
        	StringTokenizer itr = new StringTokenizer(s.toString());
							
        	String key = itr.nextToken();
			int task = Integer.parseInt(itr.nextToken());
			
			
			System.out.println("master listen : "+task+" "+key);
				
			hash.addToList(key,task);

        }
	
	}
	


}
