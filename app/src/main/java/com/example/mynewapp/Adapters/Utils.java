package com.example.mynewapp.Adapters;
import java.util.ArrayList;

public class Utils
{

    private Utils()
    {
        //Its constructor should not exist.Hence this.
    }

    public static ArrayList<String> removeDuplicatesFromList(ArrayList<String> descriptions)
    {
        ArrayList<String> tempList = new ArrayList<String>();
        for(String desc : descriptions)
        {
            if(!tempList.contains(desc))
            {
                tempList.add(desc);
            }
        }
        descriptions = tempList;
        tempList = null;
        return descriptions;
    }

}
