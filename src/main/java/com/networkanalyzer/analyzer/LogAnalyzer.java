package com.networkanalyzer.analyzer;

import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.model.NetworkLog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogAnalyzer {

    public AnalysisResult analyze(List<NetworkLog> logs){
        int totalLogs = getTotalLogs(logs);
        int successCount = 0;
        int failedCount = 0;
        Map<String,Integer> protocol = new HashMap<>();
        Map<Integer,Integer> ports = new HashMap<>();
        Map<String, Integer> sourceIp = new HashMap<>();

        for (NetworkLog log : logs){
            successCount += getSuccessCount(log);
            failedCount += getFailedCount(log);

            String protocolName = getProtocol(log);
            protocol.put(protocolName, protocol.getOrDefault(protocolName, 0)+1);

            Integer portNumber = getPort(log);
            ports.put(portNumber, ports.getOrDefault(portNumber, 0)+1);

            String sourceIP_ = getSourceIP(log);
            sourceIp.put(sourceIP_, sourceIp.getOrDefault(sourceIP_, 0)+1);
        }

        return new AnalysisResult(totalLogs,successCount,failedCount,protocol,ports,sourceIp);

    }
    public int getTotalLogs(List<NetworkLog> logs){
        return logs.size();

    }

    public int getSuccessCount(NetworkLog log){
        return "SUCCESS".equals(log.getStatus()) ? 1 : 0 ;
    }

    public int getFailedCount(NetworkLog log){
        return "FAILED".equals(log.getStatus()) ? 1 : 0 ;
    }

    public String getProtocol(NetworkLog log){
        String protocol = log.getProtocol();
        return protocol!=null ? protocol : "UNKNOWN";
    }

    public int getPort(NetworkLog log){
        return log.getPort();
    }

    public String getSourceIP(NetworkLog log){
        return log.getSourceIp();
    }

    public void analysisResult(AnalysisResult result){
        int totalLogs = result.getTotalLogs();
        int successCount = result.getSuccessCount();
        int failedCount = result.getFailedCount();
        Map<String,Integer> protocol = result.getProtocol();
        Map<Integer,Integer> ports = result.getPorts();
        Map<String, Integer> sourceIp = result.getSourceIp();
        System.out.println("======== NETWORK ANALYSIS ========");
        System.out.println("Total Logs: "+totalLogs+"\nSuccess: "+successCount+"\nFailed: "+failedCount);
        System.out.println("\n---- Protocol Distribution ----");
        for (String protocolName : protocol.keySet()) {
            System.out.println(protocolName+" : "+protocol.get(protocolName));
        }
        System.out.println("\n---- Port Activity ----");
        for (Integer  portNumber : ports.keySet()) {
            System.out.println(portNumber+" : "+ports.get(portNumber));
        }
        System.out.println("\n---- Source IP Distribution ----");
        for (String sourceIP_ : sourceIp.keySet()) {
            System.out.println(sourceIP_+" : "+sourceIp.get(sourceIP_));
        }

    }

}
