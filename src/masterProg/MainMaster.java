
package masterProg;

import java.io.IOException;



public class MainMaster {
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		/*
		 * args[0] : "filepath/name" of workerIds
		 * args[1] : "filepath/name.jar" of workerProg
		 * args[2] : "filepath/name" of the file to work on
		 */
		
		System.out.println("MasterProg says : Hello!!");
		
		Master master = new Master();
		

		
		master.importWorkerIds(args[0]);
		
		master.testWorkers();
		
		/////master.startWorker2(args[1]);
		
		//master.countLines(args[2]); //unnecessary : is done in startNWorkers 
		
		master.startNWorkers(args[2], args[1]);
		
		System.out.println("End Master");
		
	}

}
