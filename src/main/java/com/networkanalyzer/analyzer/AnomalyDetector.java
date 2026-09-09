package com.networkanalyzer.analyzer;

import com.networkanalyzer.model.Anomaly;
import com.networkanalyzer.model.NetworkLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnomalyDetector {
    public List<Anomaly> detectAnomalies(List<NetworkLog> logs) {
        Map<String,Integer> map = new HashMap<>();
        String type = "Repeated failed attempts";
        List<Anomaly> anomalies = new ArrayList<>();
        for (NetworkLog log : logs) {
            if(log.getStatus().equals("FAILED")){
                String key = log.getSourceIp()+"->"+log.getDestinationIp();
                map.put(key,map.getOrDefault(key,0)+1);
            }
        }
        for (String entry : map.keySet()) {
            if(map.get(entry)>=3){
                String[] split = entry.split("->");
                String sourceIp = split[0];
                String destinationIp = split[1];
                int count = map.get(entry);
                String description = count+" "+"failed connection attempts detected";
                Anomaly anomaly = new Anomaly(type,sourceIp,destinationIp,description,count);
                anomalies.add(anomaly);
            }
        }
        return anomalies;
    }
}
