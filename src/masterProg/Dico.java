package masterProg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class Dico {
	
	
	private HashMap<String,ArrayList<Integer>> dict; 
    private static final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();

    public Dico() 
    { 
    	dict = new HashMap<String,ArrayList<Integer>>(); 
    }

    public HashMap<String,ArrayList<Integer>> get_dict() 
    {
    	return dict;
    }
    

    public Lock getLock() 
    {
    	return lock;
    }
    
    
    public Set<String> keySet()
    {
    	return this.dict.keySet();
    }
    
    public ArrayList<Integer> get(String key)
    {
    	return this.dict.get(key);
    }
    
    public Integer size()
    {
    	return this.dict.size();
    }

    
    public synchronized void addToList(String mapKey, Integer myItem) 
	{
		
	    ArrayList<Integer> itemsList = this.dict.get(mapKey);

	    // if list does not exist create it
	    if(itemsList == null) itemsList = new ArrayList<Integer>();
	     
	    itemsList.add(myItem);
	    
	    this.dict.put(mapKey, itemsList);
	    
	}
    
    


}
