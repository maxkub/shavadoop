
package masterProg;

import java.io.IOException;



public class MainMaster {
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		/*
		 * args[0] : file of workerIds ("filepath/name")
		 * args[1] : file to work on ("filepath/name")
		 * args[2] : number of lines to take in the sliced files
		 */
		
		System.out.println("MasterProg says : Hello!!");
		
		Master master = new Master();
		

		master.importWorkerIds(args[0]);
		master.testWorkers();
		master.sliceFile(args[1], args[2]);
		
		System.out.println("Start Mappers");
		master.startMapers_withThreads(args[1]);
		
		System.out.println("Start Reducers");
		master.startReducers_withThreads();
		
		master.printDicos();
		
		master.writeMapRedOutputs(args[1]);
		
		System.out.println("Master : tout est fini");
		
	}

}
