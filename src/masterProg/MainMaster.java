
package masterProg;

import java.io.IOException;



public class MainMaster {
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		/*
		 * args[0] : file of workerIds ("filepath/name")
		 * args[1] : jar of workerProg ("filepath/name.jar")
		 * args[2] : file to work on ("filepath/name")
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
