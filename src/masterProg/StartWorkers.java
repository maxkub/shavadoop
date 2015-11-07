package masterProg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class StartWorkers {
	
	protected HashMap<String,Integer> result;
	
	public StartWorkers()
	{

	}
		
	
	public HashMap<String,Integer> getValue()
	{
		return result;
	}
	
	
	
	protected HashMap<String,Integer> listenToWorkers(BufferedReader br) throws IOException
	{
		/*
		 * listen to the workers to get their keys
		 */
		
		HashMap<String,Integer> hash = new HashMap<String,Integer>();
		
		//System.out.println("listen to worker :");
		
		String s;
		int i=0;
        while ((s = br.readLine()) != null)
        {
        	StringTokenizer itr = new StringTokenizer(s.toString());
							
        	String key = itr.nextToken();
			int task = Integer.parseInt(itr.nextToken());
			
			if(i%500==0)
			{
				System.out.println("listen to task "+task+ " : "+key);
			}
			
			
			hash.put(key, task);
			i++;
        }
        
        return hash;
	
	}
	

}
