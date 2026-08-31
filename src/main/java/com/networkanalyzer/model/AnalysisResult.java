package com.networkanalyzer.model;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class AnalysisResult {
    private int totalLogs;
    private int successCount;
    private int failedCount;
    private Map<String,Integer> protocol;
    private Map<Integer,Integer> ports;
    private Map<String, Integer> sourceIp;

    public AnalysisResult(int totalLogs, int successCount, int failedCount, Map<String, Integer> protocol, Map<Integer, Integer> ports, Map<String, Integer> sourceIp) {
        this.totalLogs = totalLogs;
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.protocol = protocol;
        this.ports = ports;
        this.sourceIp = sourceIp;
    }
}
