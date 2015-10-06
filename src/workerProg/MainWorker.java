package workerProg;

public class MainWorker {
	
	public static void main(String[] args) throws InterruptedException
	{
		System.out.println("WorkerProg says : Hello!!");
		
		long startTime = System.nanoTime();     
		
		Thread.sleep(10000);
		
		double estimatedTime = ( System.nanoTime() - startTime) * 10e-10;
		
		System.out.println("WorkerProg says : End compute after " + estimatedTime + " s");
	}

}
