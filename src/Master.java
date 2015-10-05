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
	
	public void scanNetwork(int N, String fileName) throws IOException
	{
		/*
		 * Scan port22 on N machines randomly and return IPs of the open ones, which are saved in fileName
		 */
		
		String[] command = {"/bin/sh", "-c", "nmap -p22 -oG nmap.res -iR "+ N };
		Runtime.getRuntime().exec(command);
		
		command[2] = "grep 'open' nmap.res | grep -o " +
				"'[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}' > "+ fileName ;
		Runtime.getRuntime().exec(command);
		
		importWorkerIds(fileName);
		
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
		
		String[] command = {"/bin/sh", "-c", " "};
		
		for(String worker : workerIds)
		{
			command[2] = "nc -z "+worker+" 22 | echo $?";
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
