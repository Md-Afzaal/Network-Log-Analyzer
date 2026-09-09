package com.networkanalyzer.analyzer;

import com.networkanalyzer.model.Anomaly;
import com.networkanalyzer.model.NetworkLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnomalyDetector {
    public List<Anomaly> detectAnomalies(List<NetworkLog> logs) {

        // Stores the number of FAILED connection attempts for each
        // source IP -> destination IP pair.
        Map<String,Integer> map = new HashMap<>();

        // Name of the anomaly generated when a connection pair
        // has multiple failed attempts.
        String type = "Repeated failed attempts";

        // Stores all anomalies detected from the provided logs.
        List<Anomaly> anomalies = new ArrayList<>();

        // First pass: consider only FAILED connections and count
        // how many times each source -> destination pair appears.
        for (NetworkLog log : logs) {
            if(log.getStatus().equals("FAILED")){

                // Combine source and destination IP to create a unique
                // identifier for a connection pair.
                String key = log.getSourceIp()+"->"+log.getDestinationIp();

                // Increment the failure count for this connection pair.
                // If the pair has not appeared before, start its count at 0.
                map.put(key,map.getOrDefault(key,0)+1);
            }
        }

        // Second pass: check the failure counts and create an Anomaly
        // object for every connection pair with 3 or more failures.
        for (String entry : map.keySet()) {
            if(map.get(entry)>=3){

                // Split the connection key back into source and destination IPs.
                String[] split = entry.split("->");
                String sourceIp = split[0];
                String destinationIp = split[1];

                // Get the total number of failed attempts for this pair.
                int count = map.get(entry);

                // Create a description for the anomaly.
                String description = count+" "+"failed connection attempts detected";

                // Store the detected anomaly in the result list.
                Anomaly anomaly = new Anomaly(
                        type,
                        sourceIp,
                        destinationIp,
                        description,
                        count
                );

                anomalies.add(anomaly);
            }
        }

        // Return all detected anomalies to the caller.
        return anomalies;
    }
}
