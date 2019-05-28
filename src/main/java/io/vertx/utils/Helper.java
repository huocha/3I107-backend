package io.vertx.utils;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Helper {

    public static List<String> intersection(List<List<String>> inputArrays)
    {   
 
        //Creating HashSet object for first input array
        
        List<String> intersectionSet = new ArrayList<>(inputArrays.get(0));
         
        //Calling retainAll() method of first object by passing 2nd, 3rd, 4th... objects
         
        for (int i = 1; i < inputArrays.size(); i++) 
        {
            List<String> set = new ArrayList<>(inputArrays.get(i));
             
            intersectionSet.retainAll(set);
        }
        return intersectionSet;
         
    }
}
