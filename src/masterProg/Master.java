
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
	private int available_machines;
	
	private HashMap<Integer,String> UMxMachines = new HashMap<Integer,String>();
	//private HashMap<String,ArrayList<Integer>> keyUMx = new HashMap<String,ArrayList<Integer>>();
	private static Dico keyUMx = new Dico();
	private HashMap<String,String> RMxMachines = new HashMap<String,String>();
	private static Dico mapRedOutputs = new Dico();
	
	
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
		
		available_machines = 0;
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
	        		available_machines++;
	        		
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
	
	
	public void sliceFile1(String fileName)
	{
		/*
		 * Slice the file to analyze (line by line), each line is printed in a new file.
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
	
	
	
	
	
	
	public void sliceFile(String fileName, String numlines)
	{
		/*
		 * Slice the file to analyze by groups of size numlines, each group is printed in a new file.
		 */

		int numline = Integer.parseInt(numlines); 
		
		File file = new File(fileName);
	    
	    try 
	    {
	        Scanner sc = new Scanner(file);   

	        int i = 0;
	        while (sc.hasNextLine()) 
	        {
	            
	            PrintWriter writer = new PrintWriter(fileName+i);
	            
	            int j = 0;
	            while (sc.hasNextLine() && j<numline)
	            {
	            	String line = sc.nextLine();
		            writer.println(line);
		            j++;
	            }
	            
	            writer.close();
	            
	            i++;
	       
	        }
	        
	        taskNum = i;
	        
	        System.out.println("number of tasks = "+taskNum);
	        
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
		 * Start N workers on the N first machines from the list of available ones.
		 * If there more tasks than machines, we loop on the available machines.
		 */
		
		Process[] workers = new Process[taskNum];
		
		int machine = 0;
		
		for( int i=0;i<taskNum;i++)
		{
			ProcessBuilder pb = new ProcessBuilder("ssh", workerIds.get(machine), 
					"java -jar ~/shavadoopMapper.jar "+filePath+" "+ i);//.inheritIO();
				
			workers[i] = pb.start() ;
			
			UMxMachines.put(i, workerIds.get(machine));	
			
			BufferedReader br = new BufferedReader(new InputStreamReader(workers[i].getInputStream()));
			
			listenToWorkers(br, keyUMx);
			
			machine++;
			
			if (machine == available_machines) machine = 0;
			
	
		}
			
		for(Process p : workers)
		{
			p.waitFor();
		}
		
	}
	
	
	
	
	
	
	
	
	public void startMapers2(String filePath) throws IOException, InterruptedException
	{
		/*
		 * Start N workers on the N first machines from the list of available ones.
		 * If there more tasks than machines, we loop on the available machines.
		 */
		
		Process[] workers = new Process[taskNum];
		BufferedReader[] br = new BufferedReader[taskNum];
		
		int machine = 0;
		
		for( int i=0;i<taskNum;i++)
		{
			ProcessBuilder pb = new ProcessBuilder("ssh", workerIds.get(machine), 
					"java -jar ~/shavadoopMapper.jar "+filePath+" "+ i);//.inheritIO();
				
			workers[i] = pb.start() ;
			
			UMxMachines.put(i, workerIds.get(machine));	
			
			br[i] = new BufferedReader(new InputStreamReader(workers[i].getInputStream()));
			
			machine++;
			
			if (machine == available_machines) machine = 0;
			
	
		}
		
		listenToWorkers2(br, keyUMx);
			
		for(Process p : workers)
		{
			p.waitFor();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	public void startMapers_withThreads(String filePath) throws IOException, InterruptedException
	{
		/*
		 * Start N workers on the N first machines from the list of available ones.
		 * If there more tasks than machines, we loop on the available machines.
		 */
		
		Thread[] mappers = new Thread[taskNum];
		StartMappers[] sm = new StartMappers[taskNum];
		
		int machine = 0;
		
		for( int i=0;i<taskNum;i++)
		{
			
			sm[i] = new StartMappers(workerIds.get(machine), filePath+" "+ i);
			mappers[i] = new Thread(sm[i]);
			mappers[i].start();
			
			UMxMachines.put(i, workerIds.get(machine));	
			
			machine++;
			
			if (machine == available_machines) machine = 0;
	
		}
		
		System.out.println("All Threads started");
			
		for(Thread t : mappers)
		{
			t.join();
		}
		
		for(StartMappers s : sm)
		{
			HashMap<String,Integer> result  = new HashMap<String,Integer>();
			result = s.getValue();
			
			for(String key : result.keySet())
			{
				keyUMx.addToList(key, result.get(key));
			}
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void startMapers_withTest(String filePath) throws IOException, InterruptedException
	{
		
		/*
		 * A test is done before each task launch to check the availability of the machine
		 * ==> This is very slow...
		 */
		
		ArrayList<String> workersForMap = new ArrayList<String>();
		
		// copying the workerIds Array
		for(String ids : workerIds) 
		{
		    workersForMap.add(ids);
		}
		
		
		Process[] workers = new Process[taskNum];
		
		for( int i=0;i<taskNum;i++)
		{
			
			int j =0;
			
			outerloop:
			for(String workerId : workersForMap)
			{	
				System.out.println(j);
				
				String[] command = {"/bin/sh", "-c", "ssh -o ConnectTimeout=1 "+workerId+" 'echo 2>&1' && echo OK || echo NOK"};
				
				Process p = Runtime.getRuntime().exec(command);
					
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				String s;
				//s = br.readLine();
				while ( (s = br.readLine()) != null)
		        {
	
		        
				
				System.out.println("read "+s+" "+j);
		        	
		        if(s.equals("OK"))
		        {
		        	
		        	System.out.println("worker "+workersForMap.get(j));
		        		
		        	ProcessBuilder pb = new ProcessBuilder("ssh", workersForMap.get(j), 
							"java -jar ~/shavadoopMapper.jar "+filePath+" "+ i);//.inheritIO();
						
					workers[i] = pb.start() ;
					
					UMxMachines.put(i, workersForMap.get(j));	
					
					BufferedReader br_worker = new BufferedReader(new InputStreamReader(workers[i].getInputStream()));
					
					listenToWorkers(br_worker, keyUMx);
					
					workersForMap.remove(j);
		        	
		        	break outerloop; // exit the loop on workerIds
		        		
		        }
		        else if (s.equals("NOK") || s.equals(null))
		        {
		        	System.out.println("worker "+workersForMap.get(j)+" "+s);
		        }
		        
		        }
		        
		        j++;
		        
		        
			}
			
		}
		
			
		for(Process p : workers)
		{
			p.waitFor();
		}
		
	}
	
	
	
	
	
	
	public void startReducers() throws IOException, InterruptedException
	{
		/*
		 * If there more tasks than machines, we loop on the available machines.
		 */
		
		
		Process[] workers = new Process[keyUMx.size()];
		BufferedReader[] br = new BufferedReader[keyUMx.size()];
		
		int machine = 0;
		int worker = 0;
		for(String key : keyUMx.keySet())
		{
			
			String tempfilesIDs = "";
			for(int i : keyUMx.get(key))
			{
				tempfilesIDs += " "+i;
			}
			
			//System.out.println(tempfilesIDs);
			

			ProcessBuilder pb = new ProcessBuilder("ssh", workerIds.get(machine), 
					"java -jar ~/shavadoopReducer.jar "+key+" "+ tempfilesIDs);//.inheritIO();
			
			workers[worker] = pb.start() ;
			
			RMxMachines.put(key, workerIds.get(machine));	
			
			br[worker] = new BufferedReader(new InputStreamReader(workers[worker].getInputStream()));
			
			
			
			machine++;
			worker++;
			
			if (machine == available_machines) machine = 0;
	
		}
		
		listenToWorkers2(br, mapRedOutputs);
		
		for(Process p : workers)
		{
			p.waitFor();
		}
		
	}
	
	

	
	
	public void startReducers_withThreads() throws IOException, InterruptedException
	{
		/*
		 * If there more tasks than machines, we loop on the available machines.
		 */
	
		
		Thread[] reducers = new Thread[keyUMx.get_size()];
		StartReducers[] sr = new StartReducers[keyUMx.get_size()];
		
		
		int machine = 0;
		int worker = 0;
		for(String key : keyUMx.keySet())
		{
			
			String tempfilesIDs = "";
			for(int i : keyUMx.get(key))
			{
				tempfilesIDs += " "+i;
			}
			
			//System.out.println("reducer "+keyUMx.get_size()+" "+worker);
			
			sr[worker] = new StartReducers(key, workerIds.get(machine), tempfilesIDs);
			reducers[worker] = new Thread(sr[worker]);
			reducers[worker].start();
			
			RMxMachines.put(key, workerIds.get(machine));		
			
			machine++;
			worker++;
			
			if (machine == available_machines) machine = 0;
	
		}
		
		
		for(Thread t : reducers)
		{
			t.join();
		}
		
		for(StartReducers s : sr)
		{
			HashMap<String,Integer> result  = new HashMap<String,Integer>();
			result = s.getValue();
			
			for(String k : result.keySet())
			{
				mapRedOutputs.addToList(k, result.get(k));
			}
		}
		
	}
	
	
	
	

	
	public void startReducers2(int N) throws IOException, InterruptedException
	{
		/*
		 * If there more tasks than machines, we loop on the available machines.
		 * Sending group of keys to reducers.
		 */
		
		
		Process[] workers = new Process[keyUMx.size()];
		
		int machine = 0;
		int worker = 0;
		for(String key : keyUMx.keySet())
		{
			int count = 0;
			
			for(int k=0;k<N;k++)
			{
				String tempfilesIDs = key;
				for(int i : keyUMx.get(key))
				{
					tempfilesIDs += " "+i;
				}
				tempfilesIDs += ",";
				
				
				System.out.println(tempfilesIDs);
				

				ProcessBuilder pb = new ProcessBuilder("ssh", workerIds.get(machine), 
						"java -jar ~/shavadoopReducer.jar "+key+" "+ tempfilesIDs);//.inheritIO();
				
					
				workers[worker] = pb.start() ;
				
				RMxMachines.put(key, workerIds.get(machine));	
				
				BufferedReader br = new BufferedReader(new InputStreamReader(workers[worker].getInputStream()));
				
				listenToWorkers(br, mapRedOutputs);
				
			}
			
			machine++;
			worker++;
			
			if (machine == available_machines) machine = 0;
	
		}
		
		for(Process p : workers)
		{
			p.waitFor();
		}
		
	}
	
	
	
	
	
	
	
	
	public void startReducers_withTest() throws IOException, InterruptedException
	{
		/*
		 * A test is done before each task launch to check the availability of the machine
		 * ==> This is very slow...
		 */
		ArrayList<String> workersForRed = new ArrayList<String>();
		
		// copying the workerIds Array
		for(String ids : workerIds) 
		{
		    workersForRed.add(ids);
		}
		
		Process[] workers = new Process[keyUMx.size()];
		
		int worker = 0;
		for(String key : keyUMx.keySet())
		{
			
			String tempfilesIDs = "";
			for(int i : keyUMx.get(key))
			{
				tempfilesIDs += " "+i;
			}
			
			System.out.println(tempfilesIDs);
					
			
			int i =0;
			outerloop:
			for(String workerId : workersForRed)
			{	
				String[] command = {"/bin/sh", "-c", "ssh -o ConnectTimeout=1 "+workerId+" 'echo 2>&1' && echo OK || echo NOK"};
				
				Process p = Runtime.getRuntime().exec(command);
					
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				String s;
				s = br.readLine();
				while ( (s = br.readLine()) == null)
		        {
					
					if(s.equals("OK"))
			        {
			        		
			        	ProcessBuilder pb = new ProcessBuilder("ssh", workersForRed.get(i), 
			    				"java -jar ~/shavadoopReducer.jar "+key+" "+ tempfilesIDs);//.inheritIO();
			    			
			    		workers[worker] = pb.start() ;
			    			
			    		RMxMachines.put(key, workersForRed.get(i));	
			    			
			    		BufferedReader brWorker = new BufferedReader(new InputStreamReader(workers[worker].getInputStream()));
			    			
			    		listenToWorkers(brWorker, mapRedOutputs);
			    		
			    		workersForRed.remove(i);
			    		
			    		worker++;
			        		
			        	break outerloop; // exit the loop on workerIds
			        		
			        }
		        
		        }
		        
		        i++;
			
			}
			
	
		}
		
		for(Process p : workers)
		{
			p.waitFor();
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
	
	
	
	
	
	private void listenToWorkers2(BufferedReader[] br, Dico hash) throws IOException
	{
		/*
		 * listen to the workers to get their keys
		 */
		
		
		for(int i = 0;i<taskNum;i++)
		{
			String s;
	        while ((s = br[i].readLine()) != null)
	        {
	        	StringTokenizer itr = new StringTokenizer(s.toString());
								
	        	String key = itr.nextToken();
				int task = Integer.parseInt(itr.nextToken());
				
				
				System.out.println("master listen : "+task+" "+key);
					
				hash.addToList(key,task);

	        }
		}
		
		
	
	}
	
	
	
	
	public void writeMapRedOutputs(String filepath) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter("/cal/homes/mkubryk/mapredResults.txt", "UTF-8");
		
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
		System.out.println(keyUMx.get_dict());
		
		System.out.println("RMxMachines");
		System.out.println(RMxMachines);
		
		//System.out.println("MapRedOutputs");
		//System.out.println(mapRedOutputs.get_dict());
	}
	
	
	public void addToList( HashMap<String,ArrayList<Integer>> Hash, String mapKey, Integer myItem) {
		
	    ArrayList<Integer> itemsList = Hash.get(mapKey);

	    // if list does not exist create it
	    if(itemsList == null) itemsList = new ArrayList<Integer>();
	     
	    itemsList.add(myItem);
	    
	    Hash.put(mapKey, itemsList);
	    
	}
	
}
