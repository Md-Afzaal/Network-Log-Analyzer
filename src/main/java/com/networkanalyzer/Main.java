package com.networkanalyzer;

import com.networkanalyzer.analyzer.AnomalyDetector;
import com.networkanalyzer.cli.ConsoleCLI;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConsoleCLI  cli = new ConsoleCLI();
//        cli.startCLI();
        LogParser logParser = new LogParser();
        List<NetworkLog> logs = logParser.parse();
        AnomalyDetector detector = new AnomalyDetector();
        System.out.println(detector.detectAnomalies(logs));
    }
}
