package com.networkanalyzer.cli;

import com.networkanalyzer.analyzer.AnomalyDetector;
import com.networkanalyzer.analyzer.LogAnalyzer;
import com.networkanalyzer.filter.LogFilter;
import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.model.Anomaly;
import com.networkanalyzer.model.AnomalyConfig;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;
import com.networkanalyzer.sort.LogSorter;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleCLI {
    public void startCLI(){
        LogParser logParser = new LogParser();
        FilterCLI filterCli = new FilterCLI();
        SortCLI sortCli = new SortCLI();
        AnomalyCLI anomalyCli = new AnomalyCLI();
        LogFilter logFilter = new LogFilter();
        AnomalyConfig config = new AnomalyConfig(
                3,          // Failed attempt threshold
                5,          // Time window in minutes
                5,          // Port scan threshold
                1_000_000L, // High data transfer threshold
                3           // Sensitive port access threshold
        );

        AnomalyDetector anomalyDetector = new AnomalyDetector(config);
        Scanner scan = new Scanner(System.in);
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        List<NetworkLog> lst = logParser.parse();
        AnalysisResult analysis = logAnalyzer.analyze(lst);
        while(true){
            System.out.println("====================================");
            System.out.println("        NETWORK LOG ANALYZER        ");
            System.out.println("====================================");
            System.out.println();
            System.out.println(
                    "1. View Log Analysis\n" +
                    "2. Filter Logs\n" +
                    "3. Sort Logs\n" +
                    "4. View All Logs\n" +
                    "5. Detect Anomalies\n"+
                    "6. Exit\n"

            );
            System.out.print("Enter your choice: ");
            try {
                int mainMenuChoice = scan.nextInt();
                switch (mainMenuChoice) {
                    case 1:
                        analysisResult(analysis);
                        break;
                    case 2:
                        if (lst.isEmpty()){
                            System.out.println("\nNo logs available to Filter.");
                            break;
                        }
                        filterCli.filterLogsBy(lst,logFilter,scan);
                        break;
                    case 3:
                        if (lst.isEmpty()){
                            System.out.println("\nNo logs available to Sort.");
                            break;
                        }
                        sortCli.sortLogBy(lst,scan);
                        break;
                    case 4:
                        if (lst.isEmpty()){
                            System.out.println("\nNo logs available to View.");
                            break;
                        }
                        displayALlLogs(lst);
                        break;
                    case 5:
                        if (lst.isEmpty()){
                            System.out.println("\nNo logs available to View.");
                            break;
                        }
                        anomalyCli.getAnomalyBy(lst,anomalyDetector,scan);
                        break;
                    case 6:
                        System.out.println("\nExiting....");
                        return;
                    default:
                        System.out.println("\nInvalid Input. Enter a number according to the options provided.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid input. Enter a number.");
                scan.nextLine();
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
        System.out.println("Total Logs: "+totalLogs+
                "\nSuccess: "+successCount+
                "\nFailed: "+failedCount);
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

    public void displayALlLogs(List<NetworkLog> lst){
        System.out.println("\n============= All Logs =============");
        System.out.println("\nTotal Logs: "+ lst.size());
        System.out.println();

        for (NetworkLog log : lst) {
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
