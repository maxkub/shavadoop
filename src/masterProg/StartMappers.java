package masterProg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class StartMappers implements Runnable
{
	
	private String workerIP;
	private String filePath;
	
	
	public StartMappers(String workerIP, String filePath)
	{
		this.filePath = filePath;
		this.workerIP = workerIP;
	}
	
	public void run()
	{
		
		ProcessBuilder pb = new ProcessBuilder("ssh", workerIP, 
				"java -jar ~/shavadoopMapper.jar "+filePath);//.inheritIO();
				
		Process p = pb.start() ;
			
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
					
		listenToWorkers(br, keyUMx);
			
		
		p.waitFor();
		
		
	}

}
