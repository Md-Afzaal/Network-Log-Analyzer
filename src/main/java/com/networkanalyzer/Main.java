package com.networkanalyzer;

import com.networkanalyzer.analyzer.LogAnalyzer;
import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("----Network Analyzer----");
        LogParser logParser = new LogParser();
        System.out.println(logParser.parse().get(0));
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        AnalysisResult analyzer = logAnalyzer.analyze(logParser.parse());
        System.out.println(analyzer.toString());
//        System.out.println(logAnalyzer.analyze(logParser.parse()));
//        System.out.println(logAnalyzer.totalLogs(logParser.parse()));
    }
}
