package com.networkanalyzer.cli;

import com.networkanalyzer.analyzer.AnomalyDetector;
import com.networkanalyzer.model.Anomaly;
import com.networkanalyzer.model.NetworkLog;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AnomalyCLI {
    public void getAnomalyBy(List<NetworkLog> logs, AnomalyDetector anomalyDetector,Scanner scan){
        while(true){
            System.out.println("====================================");
            System.out.println("         Anomaly  Detection         ");
            System.out.println("====================================");
            System.out.println();
            System.out.println(
                    "1. Repeated failed Attempts\n" +
                    "2. Port Scan\n" +
                    "3. High Data Transfer\n" +
                    "4. Sensitive Port Access\n" +
                    "5. Detect all Anomalies\n"+
                    "6. Back\n"

            );
            System.out.print("Enter Choice: ");
            try {
                int choice = scan.nextInt();
                if (choice == 6){
                    System.out.println("\nReturning to Main Menu...\n");
                    break;
                }

                System.out.println("\n========= Anomaly  Results =========");

                switch (choice) {
                    case 1:
                        getAnomalyByRepeatedFailedAttempts(logs,anomalyDetector);
                        break;
                    case 2:
                        getAnomalyByPortScan(logs,anomalyDetector);
                        break;
                    case 3:
                        getAnomalyByHighDataTransfer(logs,anomalyDetector);
                        break;
                    case 4:
                        getAnomalyBySensitivePortAccess(logs,anomalyDetector);
                        break;
                    case 5:
                        getAllAnomalies(logs,anomalyDetector);
                        break;
                    default:
                        System.out.println("\nInvalid choice.");
                        break;
                }
            }
            catch (InputMismatchException e){
                System.out.println("\nInvalid input. Enter from only the given options");
                scan.nextLine();
            }


        }
    }

    public static void getAnomalyByRepeatedFailedAttempts(
            List<NetworkLog> logs,
            AnomalyDetector anomalyDetector
    ){
        List<Anomaly> anomalies = new ArrayList<>(anomalyDetector.detectRepeatedFailedAttempts(logs));

        if(anomalies.isEmpty()){
            System.out.println("\nNo repeated failed attempts detected.\n");
        }
        else{
            for (Anomaly anomaly: anomalies){
                String sourceIP = anomaly.getSourceIp();
                String destinationIP = anomaly.getDestinationIp();
                long count = anomaly.getCount();
                String description = anomaly.getDescription();

                System.out.println("Source IP       : "+sourceIP);
                System.out.println("Destination IP  : "+destinationIP);
                System.out.println("Count           : "+count);
                System.out.println("Description:\n"+description);
                System.out.println();
                System.out.println("------------------------------------\n");
            }
        }
    }

    public static void getAnomalyByPortScan(
            List<NetworkLog> logs,
            AnomalyDetector anomalyDetector
    ){
        List<Anomaly> anomalies = anomalyDetector.detectPortScans(logs);

        if(anomalies.isEmpty()){
            System.out.println("\n   No port scan activity detected.   \n");
        }
        else{
            for (Anomaly anomaly: anomalies){
                String sourceIP = anomaly.getSourceIp();
                String destinationIP = anomaly.getDestinationIp();
                long count = anomaly.getCount();
                String description = anomaly.getDescription();

                System.out.println("Source IP       : "+sourceIP);
                System.out.println("Destination IP  : "+destinationIP);
                System.out.println("Count           : "+count);
                System.out.println("Description:\n"+description);
                System.out.println();
                System.out.println("------------------------------------\n");
            }
        }
    }

    public static void getAnomalyByHighDataTransfer(
            List<NetworkLog> logs,
            AnomalyDetector anomalyDetector
    ){
        List<Anomaly> anomalies = anomalyDetector.detectHighBytes(logs);

        if(anomalies.isEmpty()){
            System.out.println("\n   No High Data Transfer detected   \n");
        }
        else{
            for (Anomaly anomaly: anomalies){
                String sourceIP = anomaly.getSourceIp();
                String destinationIP = anomaly.getDestinationIp();
                long count = anomaly.getCount();
                String description = anomaly.getDescription();

                System.out.println("Source IP       : "+sourceIP);
                System.out.println("Destination IP  : "+destinationIP);
                System.out.println("Count           : "+count);
                System.out.println("Description:\n"+description);
                System.out.println();
                System.out.println("------------------------------------\n");
            }
        }
    }

    public static void getAnomalyBySensitivePortAccess(
            List<NetworkLog> logs,
            AnomalyDetector anomalyDetector
    ){
        List<Anomaly> anomalies = anomalyDetector.detectSensitivePorts(logs);

        if(anomalies.isEmpty()){
            System.out.println("\nNo sensitive port scan access detected.\n");
        }
        else{
            for (Anomaly anomaly: anomalies){
                String sourceIP = anomaly.getSourceIp();
                String destinationIP = anomaly.getDestinationIp();
                long count = anomaly.getCount();
                int port = anomaly.getPort();
                String description = anomaly.getDescription();

                System.out.println("Source IP       : "+sourceIP);
                System.out.println("Destination IP  : "+destinationIP);
                System.out.println("Port            : "+port);
                System.out.println("Count           : "+count);
                System.out.println("Description:\n"+description);
                System.out.println();
                System.out.println("------------------------------------\n");
            }
        }
    }

    public static void getAllAnomalies(
            List<NetworkLog> logs,
            AnomalyDetector anomalyDetector
    ){
        List<Anomaly> anomalies = anomalyDetector.detectAnomalies(logs);

        if(anomalies.isEmpty()){
            System.out.println("\n    No Anomalies detected    \n");
        }
        else{
            for (int i = 0; i< anomalies.size();i++){
                String sourceIP = anomalies.get(i).getSourceIp();
                String destinationIP = anomalies.get(i).getDestinationIp();
                long count = anomalies.get(i).getCount();
                int port = anomalies.get(i).getPort();
                String description = anomalies.get(i).getDescription();
                String type = anomalies.get(i).getType();

                System.out.println("["+(i+1)+"] "+type);
                System.out.println("Source IP       : "+sourceIP);
                System.out.println("Destination IP  : "+destinationIP);
                if(port == -1){
                    System.out.println("Port            : N/A");
                }
                else{
                    System.out.println("Port            : "+port);

                }
                System.out.println("Count           : "+count);
                System.out.println("Description:\n"+description);
                System.out.println();
                System.out.println("------------------------------------\n");
            }
        }
    }
}


