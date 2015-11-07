package workerProg;

import java.io.IOException;
import java.util.Arrays;

public class MainWorker {
	
	public static void main(String[] args) throws InterruptedException, IOException
	{
		/*
		 * args[0] : job of the worker "map" or "reduce"
		 * 
		 * if args[0] == "map"
		 * 		args[1] : "filepath/name" of the file to work on
		 * 		args[2] : task_id : the line-number of the line on which to apply map
		 *           (in UMxMachines : is also the key linked to worker's id) 
		 *           
		 * else
		 * 		args[1] : "key"
		 * 		args[2] to args[N] : task_ids of the mappers that treated the key
		 */
		
		if(args[0].equals("map"))
		{
			Mapper mapper = new Mapper(args[1], args[2]);
			
			mapper.map();
		}
		else if(args[0].equals("reduce"))
		{
			Reducer reducer = new Reducer(args[1], Arrays.copyOfRange(args, 2, args.length));
			
			reducer.shuffle();
			
			reducer.reduce();
		}
		
		
		
		//worker.showUM();
	
	}

}
