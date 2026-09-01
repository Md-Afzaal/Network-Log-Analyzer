package com.networkanalyzer;

import com.networkanalyzer.analyzer.LogAnalyzer;
import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.parser.LogParser;


public class Main {
    public static void main(String[] args) {
        LogParser logParser = new LogParser();
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        AnalysisResult analyzer = logAnalyzer.analyze(logParser.parse());
//        int totalLogs = analyzer.getTotalLogs();
        logAnalyzer.analysisResult(analyzer);
//        System.out.println(analyzer.toString());
    }
}
