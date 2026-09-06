package com.networkanalyzer.cli;

import com.networkanalyzer.analyzer.LogAnalyzer;
import com.networkanalyzer.filter.LogFilter;
import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;
import com.networkanalyzer.sort.LogSorter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleCLI {
    public void startCLI(){
        LogParser logParser = new LogParser();
        FilterCLI filterCli = new FilterCLI();
        SortCLI sortCli = new SortCLI();
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
                        "3. Sort Logs\n" +
                        "4. View All Logs\n" +
                        "5. Exit\n");
                System.out.print("Enter your choice: ");
                int mainMenuChoice = scan.nextInt();
                switch (mainMenuChoice) {
                    case 1:
                        analysisResult(analysis);
                        break;
                    case 2:
                        filterCli.filterLogsBy(lst,logFilter,scan);
                        break;
                    case 3:
                        sortCli.sortLogBy(lst,scan);
                        break;
                    case 4:
                        viewAllLogs(lst);
                        break;
                    case 5:
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

    public void viewAllLogs(List<NetworkLog> lst){
        for(NetworkLog log : lst){
            System.out.println(log);
        }
    }
}
