package com.networkanalyzer.parser;

import com.networkanalyzer.model.NetworkLog;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.util.List;

public class LogParser {
    private static final int LOG_FIELDS = 7;
    public List<NetworkLog> parse() {
        try{
            List<NetworkLog> lst = new ArrayList<>();
            BufferedReader reader = Files.newBufferedReader(Paths.get("logs/sample.log"));
            String line;

            while ((line = reader.readLine()) != null) {
                if(line.isEmpty()){
                    continue;
                }
                String[] split = line.trim().split("\\s+");
                if(split.length != LOG_FIELDS){
                    malformedLines(line);
                    continue;
                }
                try {
                    NetworkLog net = new NetworkLog(split);
                    lst.add(net);
                }catch(DateTimeParseException | IllegalArgumentException e){
                    malformedLines(line);
                    continue;
                }
            }
            reader.close();
            return lst;

        }

        catch (Exception e){
            System.out.println("Error reading log file "+ e.getMessage());
        }
        return null;
    }
    public void malformedLines(String line){
        System.out.println("Skipping Malformed log line: "+line);
    }
}
