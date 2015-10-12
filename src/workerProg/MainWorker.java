package workerProg;

import java.io.IOException;

public class MainWorker {
	
	public static void main(String[] args) throws InterruptedException, IOException
	{
		/*
		 * args[0] : "filepath/name" of the file to work on
		 * args[1] : the line-number of the line on which to apply map
		 *           (in UMxMachines : is also the key linked to worker's id)
		 */
		
		// Tests --------------------------------------------------------------------------------
	
		/*
		System.out.println("Worker says : Hello!!");
		
		long startTime = System.nanoTime();     
		
		Thread.sleep(10000);
		
		double estimatedTime = ( System.nanoTime() - startTime) * 10e-10;
		
		System.out.println("Worker says : End compute after " + estimatedTime + " s"); 
		
		*/
		//---------------------------------------------------------------------------------------
		
		
		Worker worker = new Worker(args[0]);
		
		worker.map2(Integer.parseInt(args[1]));
		
		worker.showUM();
		
		int line = Integer.parseInt(args[1]);
		worker.map(line);
	}

}
