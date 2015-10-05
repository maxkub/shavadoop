import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Master {
	
	private ArrayList<String> workerIds = new ArrayList<String>();
	
	
	public Master()
	{
	}
	
	public void importWorkerIds(String fileName)
	{
		/*
		 * read file containing the name of workers
		 */
				
        File file = new File(fileName);
	    
	    try 
	    {
	        Scanner sc = new Scanner(file);   

	        while (sc.hasNextLine()) 
	        {
	            String i = sc.nextLine();
	            workerIds.add(i);
	        }
	        sc.close();
	    } 
	    catch (FileNotFoundException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	
	public void testWorkers() throws IOException, InterruptedException
	{
		/*
		 * Check each worker's disponibility  
		 */
		
		Process p;
		String s;
		String status = "-1";
		
		for(String worker : workerIds)
		{
			String[] command = {"/bin/sh", "-c", "nc -z "+worker+" 22 > /dev/null | echo $?"};
			p = Runtime.getRuntime().exec(command);
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
	        while ((s = br.readLine()) != null)
	        	
	        	if(s.equals("0"))
	        	{
	        		status = "available";
	        	}
	        	else
	        	{
	        		status = "out";
	        		workerIds.remove(worker);
	        	}
	        
	        System.out.println(worker+" status: " + status);
	        
	        p.waitFor();
	        System.out.println ("exit: " + p.exitValue());
	        p.destroy();
	        
		}
		
		
		
		
		
	}

}
