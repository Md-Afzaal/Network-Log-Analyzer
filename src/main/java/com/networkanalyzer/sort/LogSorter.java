package com.networkanalyzer.sort;

import com.networkanalyzer.model.NetworkLog;

import java.util.List;

public class LogSorter {

    public void quickSort(List<NetworkLog> logs,int low, int high) {
        if(low < high) {
            int pi = partition(logs, low, high);

            quickSort(logs, low, pi);
            quickSort(logs, pi+1, high);
        }
    }

    public int partition(List<NetworkLog> logs, int low, int high) {
        NetworkLog pivot = logs.get(low);
        int i = low-1,j = high+1;

        while(true) {
            do{
                i++;
            }while (logs.get(i).getTimestamp().isBefore(pivot.getTimestamp()));

            do {
                j--;
            }while (logs.get(j).getTimestamp().isAfter(pivot.getTimestamp()));

            if (i >=j) return j;

            NetworkLog tmp = logs.get(i);
            logs.set(i, logs.get(j));
            logs.set(j, tmp);
        }
    }
}
