package com.networkanalyzer.analyzer;

import com.networkanalyzer.model.Anomaly;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.sort.LogSorter;

import java.time.Duration;
import java.util.*;

public class AnomalyDetector {

    private static final int FAILED_ATTEMPT_THRESHOLD = 3;
    private static final long TIME_WINDOW_MINUTES = 5;

    public List<Anomaly> detectAnomalies(List<NetworkLog> logs) {
        List<Anomaly> anomalies = new ArrayList<>();
        anomalies.addAll(detectRepeatedFailedAttempts(logs));
        anomalies.addAll(detectPortScans(logs));
        anomalies.addAll(detectHighBytes(logs));
        anomalies.addAll(detectSensitivePorts(logs));

        // Return all detected anomalies to the caller.
        return anomalies;
    }

    public List<Anomaly> detectRepeatedFailedAttempts(List<NetworkLog> logs){

        // Stores the number of FAILED connection attempts for each
        // source IP -> destination IP pair.
        Map<String,List<NetworkLog>> groupedLogs = new HashMap<>();

        LogSorter logSorter = new LogSorter();

        // Name of the anomaly generated when a connection pair
        // has multiple failed attempts.
        String type = "Repeated failed attempts";

        List<NetworkLog> sortedLog = new ArrayList<>();

        // Stores all anomalies detected from the provided logs.
        List<Anomaly> anomalies = new ArrayList<>();

        // First pass: consider only FAILED connections and count
        // how many times each source -> destination pair appears.
        for (NetworkLog log : logs) {
            if(log.getStatus().equals("FAILED")){
                sortedLog.add(log);
            }
        }
        logSorter.quickSort(sortedLog,0,sortedLog.size()-1, Comparator.comparing(NetworkLog::getTimestamp));


        // Second pass: check the failure counts and create an Anomaly
        // object for every connection pair with 3 or more failures.
        for (NetworkLog entry : sortedLog) {
                String key = entry.getSourceIp()+"->"+entry.getDestinationIp();
                List<NetworkLog> val = groupedLogs.get(key);
                if(val==null){
                    val = new ArrayList<>();
                    groupedLogs.put(key,val);
                }
                val.add(entry);
        }

        for (List<NetworkLog> pairLogs: groupedLogs.values()){
            int start = 0;
            int maxCount = 0;
            int maxStart = 0;
            for(int end = 0; end < pairLogs.size(); end++){
                while(Duration.between(
                        pairLogs.get(start).getTimestamp(),
                        pairLogs.get(end).getTimestamp()
                ).toMinutes()>5){
                    start++;
                }
                int count = end-start+1;
                if(count>maxCount){
                    maxCount = count;
                    maxStart = start;
                }
            }
            if(maxCount>=3){
                // Create a description for the anomaly.
                String description = maxCount+" failed connection attempts detected within 5 mins";
                String sourceIp = pairLogs.get(maxStart).getSourceIp();
                String destinationIp = pairLogs.get(maxStart).getDestinationIp();
                // Store the detected anomaly in the result list.
                Anomaly anomaly = new Anomaly(
                        type,
                        sourceIp,
                        destinationIp,
                        -1,
                        description,
                        maxCount
                );

                anomalies.add(anomaly);
            }
        }
        return anomalies;
    }

    public List<Anomaly> detectPortScans(List<NetworkLog> logs){
        Map<String, Set<Integer>> map = new HashMap<>();
        List<Anomaly> anomalies = new ArrayList<>();
        String type = "Port Scan";
        for (NetworkLog log: logs){
            String key = log.getSourceIp()+"->"+log.getDestinationIp();
            Set<Integer> ports = map.get(key);
            if(ports == null){
                ports = new HashSet<>();
                map.put(key,ports);
            }
            ports.add(log.getPort());

        }
        for(String key: map.keySet()){
            if(map.get(key).size()>=5){
                // Split the connection key back into source and destination IPs.
                String[] split = key.split("->");
                String sourceIp = split[0];
                String destinationIp = split[1];

                // Get the total number of unique port for this pair.
                int count = map.get(key).size();

                // Create a description for the anomaly.
                String description = count+" unique ports connected";

                // Store the detected anomaly in the result list.
                Anomaly anomaly = new Anomaly(
                        type,
                        sourceIp,
                        destinationIp,
                        -1,
                        description,
                        count
                );

                anomalies.add(anomaly);
            }
        }
        return anomalies;
    }

    public List<Anomaly> detectHighBytes(List<NetworkLog> logs){
        Map<String,List<Long>> map = new HashMap<>();
        List<Anomaly> anomalies = new ArrayList<>();
        String type = "High Data Transfer";
        long threshold = 1_000_000L;
        for (NetworkLog log: logs){
            String key = log.getSourceIp()+"->"+log.getDestinationIp();
            long bytes = log.getBytes();
            List<Long> bytesList = map.get(key);
            if(bytesList == null){
                bytesList = new ArrayList<>();
                map.put(key,bytesList);
            }
            bytesList.add(bytes);
        }
        for(String key: map.keySet()){
                // Split the connection key back into source and destination IPs.
                String[] split = key.split("->");
                String sourceIp = split[0];
                String destinationIp = split[1];

                long sum = getTotalBytes(map.get(key));

                if(sum >= threshold){
                    // Create a description for the anomaly.
                    String description = sum+" bytes transferred between source and destination";

                    // Store the detected anomaly in the result list.
                    Anomaly anomaly = new Anomaly(
                            type,
                            sourceIp,
                            destinationIp,
                            -1,
                            description,
                            sum
                    );

                    anomalies.add(anomaly);
                }
            }

        return anomalies;
    }

    private long getTotalBytes(List<Long> list){
        long sum = 0;
        for (long bytes: list){
            sum+=bytes;
        }
        return sum;
    }

    public List<Anomaly> detectSensitivePorts(List<NetworkLog> logs){
        Map<String,Map<Integer,Integer>> map = new HashMap<>();
        List<Anomaly> anomalies = new ArrayList<>();
        String type = "Sensitive Port Access";
        Set<Integer> sensitivePorts = Set.of(21,22,23,3389);

        for(NetworkLog log: logs){
            String key = log.getSourceIp()+"->"+log.getDestinationIp();
            int port = log.getPort();
            if (sensitivePorts.contains(port)){
                Map<Integer,Integer> portMap = map.get(key);
                if(portMap == null){
                    portMap = new HashMap<>();
                }
                portMap.put(port,portMap.getOrDefault(port,0)+1);
                map.put(key,portMap);
            }
        }
        for (String key: map.keySet()){
            String[] split = key.split("->");
            String sourceIp = split[0];
            String destinationIp = split[1];
            for (int portKey: map.get(key).keySet()){
                int portCount = map.get(key).get(portKey);
                if (portCount >= 3){
                    String description = portCount+ " access attempts detected";

                    // Store the detected anomaly in the result list.
                    Anomaly anomaly = new Anomaly(
                            type,
                            sourceIp,
                            destinationIp,
                            portKey,
                            description,
                            portCount
                    );

                    anomalies.add(anomaly);
                }
            }

        }
        return anomalies;
    }
}
