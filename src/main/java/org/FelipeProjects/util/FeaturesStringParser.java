package org.FelipeProjects.util;

import java.util.ArrayList;

/**
 * Created by felipe on 09-08-16.
 */
public class FeaturesStringParser {
    public static ArrayList<Integer> parseFeatures(String featuresString)
    {
        String[] op = featuresString.split("[0-9]+");
        String[] notOp = featuresString.split(",|:");
        return createList(op, notOp);
    }

    private static ArrayList<Integer> createList(String[] op, String[] notOp)
    {
        ArrayList<Integer> featuresList = new ArrayList<>();
        op = pop(op);

        String lastToken = notOp[0];
        String newToken;
        int lastTokenInt = Integer.parseInt(lastToken);
        int newTokenInt;
        for (int i = 0; i < op.length;i++)
        {
            newToken = notOp[i+1];
            newTokenInt = Integer.parseInt(newToken);
            if (op[i].equalsIgnoreCase(":"))
            {
                fillList(featuresList, lastTokenInt, newTokenInt);
            }
            else
            {
                fillList(featuresList,newTokenInt,newTokenInt);
            }
            lastTokenInt = newTokenInt;
        }
        return featuresList;
    }

    private static String [] pop(String[] vector)
    {
        if (vector[0].equalsIgnoreCase(",") ||
                vector[0].equalsIgnoreCase(":"))
        {
            return vector;
        }
        String[] shorter = new String[vector.length-1];
        System.arraycopy(vector,1, shorter,0, vector.length-1);
        return shorter;
    }

    private static void fillList(ArrayList<Integer> list, int start, int end)
    {
        for (int i = start; i <= end; i++)
        {
            if (list.contains(i))
                continue;

            list.add(i);
        }
    }
}
