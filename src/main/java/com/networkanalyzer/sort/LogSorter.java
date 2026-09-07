package com.networkanalyzer.sort;

import com.networkanalyzer.model.NetworkLog;

import java.util.Comparator;
import java.util.List;

public class LogSorter {

    public void quickSort(List<NetworkLog> logs,int low, int high, Comparator<NetworkLog> comparator) {
        if(low < high) {
            int pi = partition(logs, low, high, comparator);

            quickSort(logs, low, pi, comparator);
            quickSort(logs, pi+1, high,comparator);
        }
    }

    public int partition(List<NetworkLog> logs, int low, int high, Comparator<NetworkLog> comparator) {
        NetworkLog pivot = logs.get(low);
        int i = low-1,j = high+1;

        while(true) {
            do{
                i++;
            }while (comparator.compare(logs.get(i), pivot) < 0);

            do {
                j--;
            }while (comparator.compare(logs.get(j), pivot) > 0);

            if (i >=j) return j;

            NetworkLog tmp = logs.get(i);
            logs.set(i, logs.get(j));
            logs.set(j, tmp);
        }
    }
}
