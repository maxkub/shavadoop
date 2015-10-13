
package masterProg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;





public class Master {
	
	private static ArrayList<String> workerIds = new ArrayList<String>();
	
	private int sliceNum;
	
	private HashMap<Integer,String> UMxMachines = new HashMap<Integer,String>();
	private HashMap<String,String> keyUMx = new HashMap<String,String>();
	private HashMap<String,String> RMxMachines = new HashMap<String,String>();
	
	
	public Master()
	{
	}
	
	public void scanNetwork(String nmapCommands, String fileName) throws IOException
	{
		/*
		 * Scan port22 on N machines randomly and return IPs of the open ones, which are saved in fileName
		 */
		
		String[] command = {"/bin/sh", "-c", "nmap -p22 -Pn -oG nmap.res "+ nmapCommands};
		Runtime.getRuntime().exec(command);
		
		command[2] = "grep 'open' nmap.res | grep 'c[0-9]\\{3\\}-[0-9]\\{2\\}' | grep -o " +
				"'[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}' > "+ fileName ;
		Runtime.getRuntime().exec(command);
		
		importWorkerIds(fileName);
		
	}
	
	public void importWorkerIds(String fileName)
	{
		/*
		 * read file containing the name of workers (IP or hostname)
		 */
				
        File file = new File(fileName);
	    
	    try 
	    {
	        Scanner sc = new Scanner(file);   

	        int k=0;
	        while (sc.hasNextLine()) 
	        {
	            String i = sc.nextLine();
	            workerIds.add(i);
	            
	            System.out.println("import "+k+" "+i);
	            k++;
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
		
		String s;
		String status = "-1";
		
		
		ArrayList<Integer> unreach = new ArrayList<Integer>();
		
		int i = 0;
		for(String worker : workerIds)
		{	
			String[] command = {"/bin/sh", "-c", "ssh "+worker+" 'echo 2>&1' && echo OK || echo NOK"};
			
			Process p = Runtime.getRuntime().exec(command);
				
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
	        while ( (s = br.readLine()) != null)
	        {
	        	
	        	if(s.equals("OK"))
	        	{
	        		status = "available";
	        	}
	        	else if (s.equals("NOK"))
	        	{
	        		status = "Not available";
	        		unreach.add(i);
	        		System.out.println("remove "+i);
	        	}
	        }
	        
	        System.out.println(worker+" status: " + status);
	        
	        p.waitFor();
	        p.destroy();
	        
	        i++;
		}
		
	
		Collections.reverse(unreach);
		
		for(int j : unreach)
		{
			workerIds.remove(j);	
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
        p.destroy();
		
	}
	
	public void startWorker2(String jarName) throws IOException, InterruptedException
	{
		/*
		 * Alternative startWorker
		 */
		
		Random generator = new Random();
		int i = generator.nextInt(workerIds.size() );
		
		ProcessBuilder pb = new ProcessBuilder("ssh", workerIds.get(i), "hostname && java -jar "+jarName +" "+ i).inheritIO();
		
		Process p = pb.start();
		
		p.waitFor();
		p.destroy();
	}
	
	
	public void sliceFile(String fileName)
	{
		/*
		 * Slice the file to analyze, each line is printed in a new file.
		 * And print a new file for each slice.
		 */

		File file = new File(fileName);
	    
	    try 
	    {
	        Scanner sc = new Scanner(file);   

	        int i = 0;
	        while (sc.hasNextLine()) 
	        {
	            String line = sc.nextLine();
	            
	            PrintWriter writer = new PrintWriter(fileName+i);
	            writer.println(line);
	            writer.close();
	            
	            i++;
	       
	        }
	        
	        sliceNum = i;
	        
	        System.out.println("number of lines = "+sliceNum);
	        
	        sc.close();
	        
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void countLines(String filePath)
	{
		/*
		 * count the number of lines in the file
		 * using linux command wc -l
		 */
		
		ProcessBuilder pb = new ProcessBuilder("wc", "-l", filePath);
		
		try 
		{
			Process p = pb.start();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String s;
	        while ((s = br.readLine()) != null)
	        {
	        	StringTokenizer itr = new StringTokenizer(s.toString());
				while(itr.hasMoreTokens())
				{	
					sliceNum = Integer.parseInt(itr.nextToken());
					itr.nextToken();			
				}
	        }
	        
	        System.out.println("number of lines = "+sliceNum);
	        	
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public void startNWorkers(String jarName, String filePath) throws IOException, InterruptedException
	{
		/*
		 * Start N workers on the N first machines from the list of available ones
		 */
		
		//sliceNum = 5;
		
		Process[] workers = new Process[sliceNum];
		BufferedReader[] br = new BufferedReader[sliceNum];
		
		
		for( int i=0;i<sliceNum;i++)
		{
			ProcessBuilder pb = new ProcessBuilder("ssh", workerIds.get(i), 
					"hostname && java -jar "+jarName+ " "+filePath+" "+ i).inheritIO();
				
			workers[i] = pb.start() ;
			
			InputStreamReader in = new InputStreamReader(workers[i].getInputStream());
			
			br[i] = new BufferedReader(in);
						
			UMxMachines.put(i, workerIds.get(i));	
		}
			
		for(Process p : workers)
		{
			p.waitFor();
		}
		
		
		listenToWorkers(br);
		
	}
	
	
	
	private void listenToWorkers(BufferedReader[] br) throws IOException
	{
		/*
		 * listen to the workers to get their keys
		 */
		
		for(BufferedReader buff : br )
		{
			
			String s;
	        while ((s = buff.readLine()) != null)
	        {
	        	StringTokenizer itr = new StringTokenizer(s.toString());
				while(itr.hasMoreTokens())
				{	
					
					String key = itr.nextToken();
					itr.nextToken();			
				}
	        }
			
		}
	
		
	}
	
	
	public void printDicos()
	{
		System.out.println("UMxMachines");
		System.out.println(UMxMachines);
		
		System.out.println("keyUMx");
		System.out.println(keyUMx);
		
		System.out.println("RMxMachines");
		System.out.println(RMxMachines);
	}
}
