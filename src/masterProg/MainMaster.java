
package masterProg;

import java.io.IOException;



public class MainMaster {
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		System.out.println("MasterProg says : Hello!!");
		
		Master master = new Master();
		
		master.scanNetwork(500, "~/masterScan.res");
		
		master.importWorkerIds(args[0]);
		
		master.testWorkers();
		
	}

}
