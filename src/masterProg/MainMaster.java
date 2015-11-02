
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
		
		master.startMapers2(args[1]);
		//master.startMapers_withTest(args[1]);
		
		master.startReducers();
		//master.startReducers_withTest();
		
		master.printDicos();
		
		master.writeMapRedOutputs(args[1]);
		
		System.out.println("Master : tout est fini");
		
	}

}
