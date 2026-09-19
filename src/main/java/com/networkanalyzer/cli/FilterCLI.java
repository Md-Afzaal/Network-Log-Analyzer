package com.networkanalyzer.cli;

import com.networkanalyzer.filter.LogFilter;
import com.networkanalyzer.model.NetworkLog;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class FilterCLI {
    public void filterLogsBy(List<NetworkLog> logs, LogFilter logFilter, Scanner scan){
        while(true){
            String target;
            System.out.println("\n=========== Filter  Logs ===========\n");
            System.out.println("1.Filter by SourceIP\n"+
                    "2.Filter by DestinationIP\n"+
                    "3.Filter by Protocol\n"+
                    "4.Filter by Port\n"+
                    "5.Filter by Status\n"+
                    "6.Back to Main Menu\n");
            System.out.print("Enter your choice: ");
            try {
                int choice = scan.nextInt();
                if (choice == 6){
                    System.out.println("\nReturning to Main Menu...\n");
                    break;
                }
                switch (choice) {
                    case 1:
                        System.out.print("\nEnter Source IP: ");
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
                    case 5:
                        System.out.print("\nEnter Status: ");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        continue;
                }
                target = scan.next();
                if(choice == 4 && !isPortValid(target)){
                    System.out.println("Invalid port. Enter a number between 0 and 65535.");
                    continue;
                }
                filterResults(logs,logFilter,choice,target);
            } catch (InputMismatchException e){
                System.out.println("Invalid input. Enter a number");
                scan.nextLine();
            }
        }
    }

    private boolean isPortValid(String target){
        try {
            int port = Integer.parseInt(target);
            return port>=0 && port<= 65535;
        }
        catch (NumberFormatException e){
            return false;
        }
    }
    public void filterResults(List<NetworkLog> lst,LogFilter logFilter, int choice, String target){
        List<NetworkLog> filteredLogLst = logFilter.filter(lst,choice,target);
        System.out.println("\n========== Filter Results ==========");
        System.out.println("\nLogs Found: "+ filteredLogLst.size());
        System.out.println();
        for (NetworkLog log : filteredLogLst) {
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
}
