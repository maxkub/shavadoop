
package masterProg;

import java.io.IOException;



public class MainMaster {
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		/*
		 * args[0] : filepath/name of workerIds
		 * args[1] : filepath/name.jar of workerProg
		 */
		
		System.out.println("MasterProg says : Hello!!");
		
		Master master = new Master();
		

		
		master.importWorkerIds(args[0]);
		
		master.testWorkers();
		
		master.startWorker2(args[1]);
		
		System.out.println("End worker");
		
	}

}
