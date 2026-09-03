package com.networkanalyzer;

import com.networkanalyzer.analyzer.LogAnalyzer;
import com.networkanalyzer.filter.LogFilter;
import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;

import java.util.List;


public class Main {
    public static void main(String[] args) {
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

        int choice = 0;
        String target;
        while(true){
            switch (choice){}
        }

    }
}
