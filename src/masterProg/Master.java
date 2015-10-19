
package masterProg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
//import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;





public class Master {
	
	private static ArrayList<String> workerIds = new ArrayList<String>();
	
	private int taskNum; // number of tasks 
	
	private HashMap<Integer,String> UMxMachines = new HashMap<Integer,String>();
	private HashMap<String,ArrayList<Integer>> keyUMx = new HashMap<String,ArrayList<Integer>>();
	private HashMap<String,String> RMxMachines = new HashMap<String,String>();
	private HashMap<String,ArrayList<Integer>> mapRedOutputs = new HashMap<String,ArrayList<Integer>>();
	
	
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
			String[] command = {"/bin/sh", "-c", "ssh -o ConnectTimeout=1 "+worker+" 'echo 2>&1' && echo OK || echo NOK"};
			
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
	        
	        taskNum = i;
	        
	        System.out.println("number of lines = "+taskNum);
	        
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
					taskNum = Integer.parseInt(itr.nextToken());
					itr.nextToken();			
				}
	        }
	        
	        System.out.println("number of lines = "+taskNum);
	        	
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	

	
	public void startMapers(String filePath) throws IOException, InterruptedException
	{
		/*
		 * Start N workers on the N first machines from the list of available ones
		 */
		
		Process[] workers = new Process[taskNum];
		
		
		for( int i=0;i<taskNum;i++)
		{
			ProcessBuilder pb = new ProcessBuilder("ssh", workerIds.get(i), 
					"java -jar ~/shavadoopMapper.jar "+filePath+" "+ i);//.inheritIO();
				
			workers[i] = pb.start() ;
			
			UMxMachines.put(i, workerIds.get(i));	
			
			BufferedReader br = new BufferedReader(new InputStreamReader(workers[i].getInputStream()));
			
			listenToWorkers(br, keyUMx);
	
		}
			
		for(Process p : workers)
		{
			p.waitFor();
		}
		
	}
	
	
	
	public void startReducers() throws IOException, InterruptedException
	{
		
		
		Process[] workers = new Process[keyUMx.size()];
		
		int machine = 0;
		for(String key : keyUMx.keySet())
		{
			
			String tempfilesIDs = "";
			for(int i : keyUMx.get(key))
			{
				tempfilesIDs += " "+i;
			}
			
			System.out.println(tempfilesIDs);
			
			ProcessBuilder pb = new ProcessBuilder("ssh", workerIds.get(machine), 
					"java -jar ~/shavadoopReducer.jar "+key+" "+ tempfilesIDs);//.inheritIO();
				
			workers[machine] = pb.start() ;
			
			RMxMachines.put(key, workerIds.get(machine));	
			
			BufferedReader br = new BufferedReader(new InputStreamReader(workers[machine].getInputStream()));
			
			listenToWorkers(br, mapRedOutputs);
			
			machine++;
	
		}
		
		for(Process p : workers)
		{
			p.waitFor();
		}
		
	}
	
	
	
	private void listenToWorkers(BufferedReader br, HashMap<String,ArrayList<Integer>> hash) throws IOException
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
				
			addToList(hash,key,task);

        }
	
	}
	
	
	public void writeMapRedOutputs(String filepath) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(filepath+"-mapredResults.txt", "UTF-8");
		
		for(String k : mapRedOutputs.keySet())
		{
			writer.println(k+" "+mapRedOutputs.get(k));
		}
		writer.close();
		
	}
	
	
	public void printDicos()
	{
		System.out.println("UMxMachines");
		System.out.println(UMxMachines);
		
		System.out.println("keyUMx");
		System.out.println(keyUMx);
		
		System.out.println("RMxMachines");
		System.out.println(RMxMachines);
		
		System.out.println("MapRedOutputs");
		System.out.println(mapRedOutputs);
	}
	
	
	public void addToList( HashMap<String,ArrayList<Integer>> Hash, String mapKey, Integer myItem) {
		
	    ArrayList<Integer> itemsList = Hash.get(mapKey);

	    // if list does not exist create it
	    if(itemsList == null) itemsList = new ArrayList<Integer>();
	     
	    itemsList.add(myItem);
	    
	    Hash.put(mapKey, itemsList);
	    
	}
	
}
