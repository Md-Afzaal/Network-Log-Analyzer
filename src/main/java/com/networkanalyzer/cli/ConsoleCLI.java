package com.networkanalyzer.cli;

import com.networkanalyzer.analyzer.LogAnalyzer;
import com.networkanalyzer.filter.LogFilter;
import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleCLI {
    public void startCLI(){
        LogParser logParser = new LogParser();
        LogFilter logFilter = new LogFilter();
        Scanner scan = new Scanner(System.in);
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        List<NetworkLog> lst = logParser.parse();
        AnalysisResult analysis = logAnalyzer.analyze(lst);
        while(true){
            try{
                System.out.println("====================================");
                System.out.println("        NETWORK LOG ANALYZER        ");
                System.out.println("====================================");
                System.out.println();
                System.out.println("1. View Log Analysis\n" +
                        "2. Filter Logs\n" +
                        "3. View All Logs\n" +
                        "4. Exit\n");
                System.out.print("Enter your choice: ");
                int mainMenuChoice = scan.nextInt();
                switch (mainMenuChoice) {
                    case 1:
                        analysisResult(analysis);
                        break;
                    case 2:
                        filterLogsBy(lst,logFilter,scan);
                        break;
                    case 3:
                        viewAllLogs(lst);
                        break;
                    case 4:
                        System.out.println("\nExiting....");
                        return;
                }

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void analysisResult(AnalysisResult result){
        int totalLogs = result.getTotalLogs();
        int successCount = result.getSuccessCount();
        int failedCount = result.getFailedCount();
        Map<String,Integer> protocol = result.getProtocol();
        Map<Integer,Integer> ports = result.getPorts();
        Map<String, Integer> sourceIp = result.getSourceIp();
        System.out.println("\n========= NETWORK ANALYSIS =========");
        System.out.println("Total Logs: "+totalLogs+"\nSuccess: "+successCount+"\nFailed: "+failedCount);
        System.out.println("\n------ Protocol  Distribution ------");
        for (String protocolName : protocol.keySet()) {
            System.out.println(protocolName+" : "+protocol.get(protocolName));
        }
        System.out.println("\n---------- Port  Activity ----------");
        for (Integer  portNumber : ports.keySet()) {
            System.out.println(portNumber+" : "+ports.get(portNumber));
        }
        System.out.println("\n------ Source IP Distribution ------");
        for (String _sourceIP : sourceIp.keySet()) {
            System.out.println(_sourceIP+" : "+sourceIp.get(_sourceIP));
        }
    }

    public void filterLogsBy(List<NetworkLog> lst,LogFilter logFilter, Scanner scan){

        while(true){
            String target;
            System.out.println("\n=========== Filter  Logs ===========\n");
            System.out.println("1.Filter by SourceIP");
            System.out.println("2.Filter by DestinationIP");
            System.out.println("3.Filter by Protocol");
            System.out.println("4.Filter by Port");
            System.out.println("5.Filter by Status");
            System.out.println("6.Back to Main Menu\n");
            System.out.print("Enter your choice: ");
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
            filterResults(lst,logFilter,choice,target);

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

    public void viewAllLogs(List<NetworkLog> lst){
        for(NetworkLog log : lst){
            System.out.println(log);
        }
    }
}
