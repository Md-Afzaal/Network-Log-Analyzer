package com.networkanalyzer.parser;

import com.networkanalyzer.model.NetworkLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.util.List;

public class LogParser {
    public List<NetworkLog> parse() {
        try{
            List<NetworkLog> lst = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(Paths.get("logs/sample.log"))));
            String line;

            while ((line = reader.readLine()) != null) {
                if(line.isEmpty()){
                    continue;
                }
                String[] split = line.split(" ");
                NetworkLog net = new NetworkLog(split);
                lst.add(net);
            }
            reader.close();
            return lst;

        }

        catch (Exception e){
            System.out.println("Error reading log file "+ e.getMessage());
        }
        return null;
    }
}
