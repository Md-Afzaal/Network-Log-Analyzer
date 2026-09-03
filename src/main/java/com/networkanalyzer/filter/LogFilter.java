package com.networkanalyzer.filter;

import com.networkanalyzer.model.NetworkLog;

import java.util.ArrayList;
import java.util.List;

public class LogFilter {
    public List<NetworkLog> filter(List<NetworkLog> logs, int choice, String target){
        List<NetworkLog> filteredLogs = new ArrayList<>();

        int targetPort = -1;

        if (choice == 4){
            targetPort = Integer.parseInt(target);
        }
        for(NetworkLog log : logs){
            switch(choice){
                case 1:
                    if(filterBySourceIP(log,target)){
                        filteredLogs.add(log);
                    }
                    break;

                case 2:
                    if(filterByDestinationIP(log,target)){
                        filteredLogs.add(log);
                    }
                    break;

                case 3:
                    if(filterByProtocol(log,target)){
                        filteredLogs.add(log);
                    }
                    break;

                case 4:
                    if(filterByPort(log,targetPort)){
                        filteredLogs.add(log);
                    }
                    break;

                case 5:
                    if(filterByStatus(log,target)){
                        filteredLogs.add(log);
                    }
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
        return filteredLogs;
    }

    public boolean filterBySourceIP(NetworkLog log, String targetSourceIp){
        String currSourceIp =  log.getSourceIp();
        return currSourceIp.equals(targetSourceIp);
    }

    public boolean filterByDestinationIP(NetworkLog log, String targetDestinationIp){
        String currDestinationIp =  log.getDestinationIp();
        return currDestinationIp.equals(targetDestinationIp);
    }

    public boolean filterByProtocol(NetworkLog log, String targetProtocol){
        String currProtocol =  log.getProtocol();
        return currProtocol.equals(targetProtocol);
    }

    public boolean filterByPort(NetworkLog log, int targetPort){
        int currPort =  log.getPort();
        return currPort == targetPort ;
    }

    public boolean filterByStatus(NetworkLog log, String targetStatus){
        String currStatus =  log.getStatus();
        return currStatus.equals(targetStatus);
    }
}
