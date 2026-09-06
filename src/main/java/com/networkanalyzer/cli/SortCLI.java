package com.networkanalyzer.cli;

import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.sort.LogSorter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class SortCLI {
    public void sortLogBy(List<NetworkLog> logs, Scanner scan) {
        LogSorter logSorter = new LogSorter();
        while(true){
            System.out.println("\n============ Sort  Logs ============\n");
            System.out.println("1.Sort by TimeStamp\n"+
                    "2.Sort by Bytes\n"+
                    "3.Sort by Port\n"+
                    "4.Sort by Status\n"+
                    "5.Back to Main Menu\n");
            System.out.print("Enter your choice: ");
            int choice = scan.nextInt();
            if (choice == 5){
                System.out.println("\nReturning to Main Menu...\n");
                break;
            }
            switch (choice) {
                case 1:
                    sortByTimeStamp(logs,logSorter,scan);
                    break;
                case 2:
                    System.out.print("\nEnter DestinationIP: ");
                    break;
                case 3:
                    System.out.print("\nEnter Protocol: ");
                    break;
                case 4:
                    System.out.print("\nEnter Port: ");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    continue;
            }

        }
    }
    public void sortByTimeStamp(List<NetworkLog> logs,LogSorter logSorter,Scanner scan) {
        int choice = 0;
        while(true){
            System.out.println("\n------------ Sort Order ------------\n");
            System.out.println("1. Oldest to Newest\n"+
                    "2. Newest to Oldest\n"+
                    "3. Back\n");
            choice = scan.nextInt();
            switch (choice) {
                case 1:
                    sortTimeStampByAscendingOrder(logs,logSorter);
                    break;
                case 2:
                    sortTimeStampByDescendingOrder(logs,logSorter);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice.");
                    return;

            }
        }

    }

    public void sortTimeStampByAscendingOrder(List<NetworkLog> logs,LogSorter logSorter) {
        logSorter.quickSort(logs,0,logs.size()-1);
        System.out.println("\n=========== Sorted  Logs ===========\n");
        for(NetworkLog log : logs){
            displaySortedLogs(log);
        }
    }

    public void sortTimeStampByDescendingOrder(List<NetworkLog> logs,LogSorter logSorter) {
        logSorter.quickSort(logs,0,logs.size()-1);
        System.out.println("\n=========== Sorted  Logs ===========\n");
        for(int i = logs.size()-1; i >= 0; i--){
            displaySortedLogs(logs.get(i));
        }
    }

    public void displaySortedLogs(NetworkLog log) {
        LocalDateTime dateTime = log.getTimestamp();
        String sourceIp = log.getSourceIp();
        String destinationIp = log.getDestinationIp();
        String protocol = log.getProtocol();
        int port = log.getPort();
        String status = log.getStatus();
        long bytes = log.getBytes();

        System.out.println(dateTime);
        System.out.println();
        System.out.println("Source IP       : "+sourceIp);
        System.out.println("Destination IP  : "+destinationIp);
        System.out.println("Protocol        : "+protocol);
        System.out.println("Port            : "+port);
        System.out.println("Status          : "+status);
        System.out.println("Bytes           : "+bytes);
        System.out.println();
        System.out.println("------------------------------------\n");
    }
}
