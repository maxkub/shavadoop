package masterProg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public class Dico {
	
	
	private static HashMap<String,ArrayList<Integer>> dict = new HashMap<String,ArrayList<Integer>>(); 
    private static final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();

    private Dico() { }

    public static HashMap<String,ArrayList<Integer>> get_dict() 
    {
    	return dict;
    }
    

    public static Lock getLock() 
    {
    	return lock;
    }

    
    public void addToList(String mapKey, Integer myItem) 
	{
		
	    ArrayList<Integer> itemsList = this.dict.get(mapKey);

	    // if list does not exist create it
	    if(itemsList == null) itemsList = new ArrayList<Integer>();
	     
	    itemsList.add(myItem);
	    
	    this.dict.put(mapKey, itemsList);
	    
	}
    
    


}
