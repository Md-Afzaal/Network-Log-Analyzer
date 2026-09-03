package com.networkanalyzer;

import com.networkanalyzer.analyzer.LogAnalyzer;
import com.networkanalyzer.filter.LogFilter;
import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;

import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        LogParser logParser = new LogParser();
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        List<NetworkLog> lst = logParser.parse();
        AnalysisResult analyzer = logAnalyzer.analyze(lst);
//        int totalLogs = analyzer.getTotalLogs();
        logAnalyzer.analysisResult(analyzer);
//        System.out.println(analyzer.toString());
        LogFilter logFilter = new LogFilter();
//        List<NetworkLog> filteredLog = logFilter.filter(lst,1);
        System.out.println("==== Enter choice ====");
        System.out.println("1.Filter by SourceIP");
        System.out.println("2.Filter by DestinationIP");
        System.out.println("3.Filter by Protocol");
        System.out.println("4.Filter by Port");
        System.out.println("5.Filter by Status");
        System.out.println("6.Quit");
        int choice = scan.nextInt();
        String target;
        if (choice == 6) {
            System.out.println("Exiting...");
        }
        else{
            System.out.println("==== Enter target ==== ");
            target = scan.next();
            System.out.println(logFilter.filter(lst,choice,target).toString());
        }
    }
}
