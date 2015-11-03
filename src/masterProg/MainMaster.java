
package masterProg;

import java.io.IOException;



public class MainMaster {
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		/*
		 * args[0] : file of workerIds ("filepath/name")
		 * args[1] : file to work on ("filepath/name")
		 * args[2] : number to lines in a slice of the file
		 */
		
		System.out.println("MasterProg says : Hello!!");
		
		Master master = new Master();
		

		
		master.importWorkerIds(args[0]);
		
		master.testWorkers();
		
		/////master.startWorker2(args[1]);
		
		//master.countLines(args[2]); //unnecessary : is done in startNWorkers 
		
		master.sliceFile_num(args[1], args[2]);
		//master.sliceFile(args[1]);
		
		master.startMapers(args[1]);
		//master.startMapers_withTest(args[1]);
		
		master.startReducers();
		//master.startReducers_withTest();
		
		master.printDicos();
		
		master.writeMapRedOutputs(args[1]);
		
		System.out.println("Master : tout est fini");
		
	}

}
