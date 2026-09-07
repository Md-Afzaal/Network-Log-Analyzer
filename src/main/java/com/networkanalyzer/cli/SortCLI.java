package com.networkanalyzer.cli;

import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.sort.LogSorter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
// resolve
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
                    sortByBytes(logs,logSorter,scan);
                    break;
                case 3:
                    sortByPort(logs,logSorter,scan);
                    break;
                case 4:
                    System.out.print("\nSort by status incoming: ");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    continue;
            }

        }
    }
    public void sortByTimeStamp(List<NetworkLog> logs,LogSorter logSorter,Scanner scan) {
        int choice = 0;
        while (true){
            System.out.println("\n------------ Sort Order ------------\n");
            System.out.println("1. Oldest to Newest\n"+
                    "2. Newest to Oldest\n"+
                    "3. Back\n");
            System.out.print("Enter your choice: ");
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
                    break;

            }
        }
    }

    public void sortTimeStampByAscendingOrder(List<NetworkLog> logs,LogSorter logSorter) {
        logSorter.quickSort(logs,0,logs.size()-1, Comparator.comparing(NetworkLog::getTimestamp));
        System.out.println("\n=========== Sorted  Logs ===========\n");
        for(NetworkLog log : logs){
            displaySortedLogs(log);
        }
    }

    public void sortTimeStampByDescendingOrder(List<NetworkLog> logs,LogSorter logSorter) {
        logSorter.quickSort(logs,0,logs.size()-1, Comparator.comparing(NetworkLog::getTimestamp));
        System.out.println("\n=========== Sorted  Logs ===========\n");
        for(int i = logs.size()-1; i >= 0; i--){
            displaySortedLogs(logs.get(i));
        }
    }

    public void sortByBytes(List<NetworkLog> logs,LogSorter logSorter,Scanner scan) {
        int choice = 0;
        while (true){
            System.out.println("\n------------ Sort Order ------------\n");
            System.out.println("1. Smallest to Largest\n"+
                    "2. Largest to Smallest\n"+
                    "3. Back\n");
            System.out.print("Enter your choice: ");
            choice = scan.nextInt();
            switch (choice) {
                case 1:
                    sortBytesByAscendingOrder(logs,logSorter);
                    break;
                case 2:
                    sortBytesByDescendingOrder(logs,logSorter);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;

            }
        }
    }
    public void sortBytesByAscendingOrder(List<NetworkLog> logs,LogSorter logSorter) {
        logSorter.quickSort(logs,0,logs.size()-1, Comparator.comparingLong(NetworkLog::getBytes));
        System.out.println("\n=========== Sorted  Logs ===========\n");
        for(NetworkLog log : logs){
            displaySortedLogs(log);
        }
    }

    public void sortBytesByDescendingOrder(List<NetworkLog> logs,LogSorter logSorter) {
        logSorter.quickSort(logs,0,logs.size()-1, Comparator.comparingLong(NetworkLog::getBytes));
        System.out.println("\n=========== Sorted  Logs ===========\n");
        for(int i = logs.size()-1; i >= 0; i--){
            displaySortedLogs(logs.get(i));
        }
    }

    public void sortByPort(List<NetworkLog> logs,LogSorter logSorter,Scanner scan) {
        int choice = 0;
        while (true){
            System.out.println("\n------------ Sort Order ------------\n");
            System.out.println("1. Smallest to Largest\n"+
                    "2. Largest to Smallest\n"+
                    "3. Back\n");
            System.out.print("Enter your choice: ");
            choice = scan.nextInt();
            switch (choice) {
                case 1:
                    sortPortByAscendingOrder(logs,logSorter);
                    break;
                case 2:
                    sortPortByDescendingOrder(logs,logSorter);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;

            }
        }
    }

    public void sortPortByAscendingOrder(List<NetworkLog> logs,LogSorter logSorter) {
        logSorter.quickSort(logs,0,logs.size()-1, Comparator.comparingLong(NetworkLog::getPort));
        System.out.println("\n=========== Sorted  Logs ===========\n");
        for(NetworkLog log : logs){
            displaySortedLogs(log);
        }
    }

    public void sortPortByDescendingOrder(List<NetworkLog> logs,LogSorter logSorter) {
        logSorter.quickSort(logs,0,logs.size()-1, Comparator.comparingLong(NetworkLog::getPort));
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
