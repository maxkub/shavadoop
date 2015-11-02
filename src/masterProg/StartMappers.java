package masterProg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class StartMappers implements Runnable
{
	
	private String workerIP;
	private String filePath;
	private HashMap<String,ArrayList<Integer>> keyUMx;
	private HashMap<String,ArrayList<Integer>> dict;
	
	public StartMappers(String workerIP, String filePath, HashMap<String,ArrayList<Integer>> keyUMx)
	{
		this.filePath = filePath;
		this.workerIP = workerIP;
		this.keyUMx   = keyUMx;
	}
	
	
	
	public void run()
	{
		
		ProcessBuilder pb = new ProcessBuilder("ssh", workerIP, 
				"java -jar ~/shavadoopMapper.jar "+filePath);//.inheritIO();
				
		try 
		{
			Process p = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			//listenToWorkers(br, keyUMx);
			
			
			synchronized(Dico.get_dict()) 
			{

	            Dico dict = Dico.get_dict();
	            listenToWorkers(br, dict);

	        }	
			
			
		} 
		catch (IOException e) 
		{

			e.printStackTrace();
		} 
		catch (InterruptedException e) 
		{

			e.printStackTrace();
		}
			
		
	}
	
	public HashMap<String,ArrayList<Integer>> get_dict()
	{
		return dict;
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
