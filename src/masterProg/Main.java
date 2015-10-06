
package masterProg;

import java.io.IOException;



public class Main {
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		System.out.println("Hello");
		
		Master master = new Master();
		
		master.importWorkerIds(args[0]);
		
		master.testWorkers();
		
	}

}
