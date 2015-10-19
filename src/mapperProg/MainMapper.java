package mapperProg;

import java.io.IOException;


public class MainMapper {

	public static void main(String[] args) throws InterruptedException, IOException
	{
		/*
		 * args[0] : "filepath/name" of the file to work on
		 * args[1] : task_id : the line-number of the line on which to apply map
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
		
		
		Mapper mapper = new Mapper(args[0], args[1]);
		
		mapper.map();
		
		
		//worker.showUM();
	
	}
}
