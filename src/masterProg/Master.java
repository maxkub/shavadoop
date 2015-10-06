
package masterProg;

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
		
		String[] command = {"/bin/sh", "-c", "nmap -p22 -Pn -oG nmap.res -iR "+ N };
		Runtime.getRuntime().exec(command);
		
		command[2] = "grep 'open' nmap.res | grep 'c[0-9]\\{3\\}-[0-9]\\{2\\}' | grep -o " +
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
	    
	    //for(String el : workerIds)
	    //{
	    //	System.out.println(el);
	    //}
	}
	
	
	public void testWorkers() throws IOException, InterruptedException
	{
		/*
		 * Check each worker's disponibility  
		 */
		
		String s;
		String status = "-1";
		
		
		ArrayList<Integer> unreach = new ArrayList<Integer>();
		
		int i = 0;
		for(String worker : workerIds)
		{	
			String[] command = {"/bin/sh", "-c", "ssh "+worker+" 'echo 2>&1' && echo OK || echo NOK"};
			
			Process p = Runtime.getRuntime().exec(command);
				
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
	        while ((s = br.readLine()) != null)
	        	
	        	if(s.equals("OK"))
	        	{
	        		status = "available";
	        	}
	        	else
	        	{
	        		status = "NOK";
	        		unreach.add(i);
	        	}
	        
	        System.out.println(worker+" status: " + status);
	        
	        p.waitFor();
	        System.out.println ("exit: " + p.exitValue());
	        p.destroy();
	        
	        i++;
		}
		
		for(int j : unreach)
		{
			try
			{
				workerIds.remove(j);
			}catch (IndexOutOfBoundsException e)
			{
				System.out.println(e.getMessage());
			}	
		}
	}
	
	
	public void startWorker(String jarName) throws IOException, InterruptedException
	{
		Process p;
		String s;
		
		String[] command = {"/bin/sh", "-c", "ssh "+workerIds.get(0)+" 'hostname && java -jar "+jarName+"'"};
		p = Runtime.getRuntime().exec(command);
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
        while ((s = br.readLine()) != null)
        	System.out.println(s);
        
        p.waitFor();
        System.out.println ("exit: " + p.exitValue());
        p.destroy();
		
	}
	
	public void startWorker2(String jarName) throws IOException
	{
		/*
		 * Alternative startWorker
		 */
		
		ProcessBuilder pb = new ProcessBuilder("ssh", workerIds.get(0), "hostname && java -jar "+jarName).inheritIO();
		
		pb.start();
	}

}
