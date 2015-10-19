package reducerProg;

import java.io.IOException;
import java.util.Arrays;

public class MainReducer {
	
	public static void main(String[] args) throws InterruptedException, IOException
	{
		/*
		 * args[0] : "key"
		 * args[1] .. args[N] : task_ids of the mappers that treated the key
		 */
		
		//System.out.println("Reducer Started");
		
		Reducer reducer = new Reducer(args[0], Arrays.copyOfRange(args, 1, args.length));
		
		reducer.shuffle();
		
		reducer.reduce();
	
	}

}
