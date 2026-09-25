package com.networkanalyzer.analyzer;

import com.networkanalyzer.model.Anomaly;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.sort.LogSorter;

import java.time.Duration;
import java.util.*;

public class AnomalyDetector {

    // Minimum number of failed attempts required to trigger
    // the repeated failed attempts anomaly.
    private static final int FAILED_ATTEMPT_THRESHOLD = 3;

    // Maximum time interval allowed between the first and last
    // failed attempt in a repeated-failure window.
    private static final long TIME_WINDOW_MINUTES = 5;

    /**
     * Runs all available anomaly detection rules on the provided logs.
     *
     * @param logs list of network logs to analyze
     * @return list containing all detected anomalies
     */
    public List<Anomaly> detectAnomalies(List<NetworkLog> logs) {

        // Store anomalies detected by all detection rules.
        List<Anomaly> anomalies = new ArrayList<>();

        // Run each anomaly detection rule and add its results
        // to the common anomaly list.
        anomalies.addAll(detectRepeatedFailedAttempts(logs));
        anomalies.addAll(detectPortScans(logs));
        anomalies.addAll(detectHighBytes(logs));
        anomalies.addAll(detectSensitivePorts(logs));

        // Return all detected anomalies to the caller.
        return anomalies;
    }

    /**
     * Detects repeated failed connection attempts between the same
     * source and destination within a five-minute time window.
     *
     * @param logs list of network logs to analyze
     * @return anomalies representing repeated failed attempts
     */
    public List<Anomaly> detectRepeatedFailedAttempts(List<NetworkLog> logs) {

        // Groups failed logs by source IP -> destination IP.
        // Each group is analyzed independently for a time-window violation.
        Map<String, List<NetworkLog>> groupedLogs = new HashMap<>();

        LogSorter logSorter = new LogSorter();

        // Name of the anomaly generated when multiple failed
        // connection attempts are detected within the time window.
        String type = "Repeated failed attempts";

        // Temporary list containing only FAILED connection attempts.
        List<NetworkLog> sortedLog = new ArrayList<>();

        // Stores all repeated-failure anomalies detected.
        List<Anomaly> anomalies = new ArrayList<>();

        // First pass: keep only failed connection attempts because
        // successful connections are irrelevant to this detection rule.
        for (NetworkLog log : logs) {
            if (log.getStatus().equals("FAILED")) {
                sortedLog.add(log);
            }
        }

        // Sort failed attempts chronologically so that a sliding-window
        // approach can be used to find repeated attempts within five minutes.
        logSorter.quickSort(
                sortedLog,
                0,
                sortedLog.size() - 1,
                Comparator.comparing(NetworkLog::getTimestamp)
        );

        // Group the sorted failed attempts by source -> destination pair.
        // This prevents failed attempts involving different connections
        // from being counted together.
        for (NetworkLog entry : sortedLog) {

            String key = entry.getSourceIp() + "->" + entry.getDestinationIp();

            List<NetworkLog> val = groupedLogs.computeIfAbsent(key, k -> new ArrayList<>());

            val.add(entry);
        }

        // Analyze each source -> destination group independently.
        for (List<NetworkLog> pairLogs : groupedLogs.values()) {

            // Start of the current sliding window.
            int start = 0;

            // Stores the largest number of failed attempts found
            // within any valid five-minute window.
            int maxCount = 0;

            // Stores the starting position of the strongest window.
            int maxStart = 0;

            // Expand the sliding window one log at a time.
            for (int end = 0; end < pairLogs.size(); end++) {

                // Move the start of the window forward whenever the
                // time difference becomes greater than five minutes.
                while (Duration.between(
                        pairLogs.get(start).getTimestamp(),
                        pairLogs.get(end).getTimestamp()
                ).toMinutes() > TIME_WINDOW_MINUTES) {

                    start++;
                }

                // Number of failed attempts currently inside the window.
                int count = end - start + 1;

                // Keep the strongest window found for this connection pair.
                if (count > maxCount) {
                    maxCount = count;
                    maxStart = start;
                }
            }

            // Create an anomaly only when the number of failed attempts
            // reaches the configured threshold.
            if (maxCount >= FAILED_ATTEMPT_THRESHOLD) {

                // Describe how many failed attempts were detected
                // within the configured time window.
                String description =
                        maxCount + " failed connection attempts detected within "
                                + TIME_WINDOW_MINUTES + " mins";

                // Get the source and destination belonging to the
                // strongest detected time window.
                String sourceIp = pairLogs.get(maxStart).getSourceIp();
                String destinationIp = pairLogs.get(maxStart).getDestinationIp();

                // Create and store the detected anomaly.
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

    /**
     * Detects possible port scans by counting the number of unique
     * ports accessed between each source and destination pair.
     *
     * @param logs list of network logs to analyze
     * @return anomalies representing possible port scans
     */
    public List<Anomaly> detectPortScans(List<NetworkLog> logs) {

        // Maps each source -> destination pair to the set of
        // unique ports accessed.
        Map<String, Set<Integer>> map = new HashMap<>();

        // Stores detected port-scan anomalies.
        List<Anomaly> anomalies = new ArrayList<>();

        String type = "Port Scan";

        // Process every network log.
        for (NetworkLog log : logs) {

            // Create a unique key for the source and destination pair.
            String key = log.getSourceIp() + "->" + log.getDestinationIp();

            // Create a new set when this connection pair is encountered
            // for the first time.
            Set<Integer> ports = map.computeIfAbsent(key, k -> new HashSet<>());

            // Add the current port. HashSet automatically ignores
            // duplicate port numbers.
            ports.add(log.getPort());
        }

        // Check every connection pair for the port-scan threshold.
        for (String key : map.keySet()) {

            // A connection pair accessing five or more unique ports
            // is considered a possible port scan.
            if (map.get(key).size() >= 5) {

                // Split the connection key back into source and destination IPs.
                String[] split = key.split("->");

                String sourceIp = split[0];
                String destinationIp = split[1];

                // Number of unique ports accessed by this connection pair.
                int count = map.get(key).size();

                // Create a description for the detected anomaly.
                String description = count + " unique ports connected";

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

    /**
     * Detects unusually high data transfer between a source
     * and destination pair.
     *
     * @param logs list of network logs to analyze
     * @return anomalies representing high data transfer
     */
    public List<Anomaly> detectHighBytes(List<NetworkLog> logs) {

        // Maps each source -> destination pair to all bytes
        // transferred between them.
        Map<String, List<Long>> map = new HashMap<>();

        // Stores detected high-data-transfer anomalies.
        List<Anomaly> anomalies = new ArrayList<>();

        String type = "High Data Transfer";

        // Minimum total number of bytes required to trigger
        // the high-data-transfer anomaly.
        long threshold = 1_000_000L;

        // Process every network log.
        for (NetworkLog log : logs) {

            // Create a unique source -> destination key.
            String key = log.getSourceIp() + "->" + log.getDestinationIp();

            long bytes = log.getBytes();

            // Create a new list for a previously unseen connection pair.
            List<Long> bytesList = map.computeIfAbsent(key, k -> new ArrayList<>());

            // Store the bytes transferred by this connection.
            bytesList.add(bytes);
        }

        // Check the total data transferred for every connection pair.
        for (String key : map.keySet()) {

            // Split the connection key back into source and destination IPs.
            String[] split = key.split("->");

            String sourceIp = split[0];
            String destinationIp = split[1];

            // Calculate the total bytes transferred between the pair.
            long sum = getTotalBytes(map.get(key));

            // Generate an anomaly when the total reaches the threshold.
            if (sum >= threshold) {

                // Create a description containing the total transferred data.
                String description =
                        sum + " bytes transferred between source and destination";

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

    /**
     * Calculates the total number of bytes from a list of byte values.
     *
     * @param list list containing byte values
     * @return total number of bytes
     */
    private long getTotalBytes(List<Long> list) {

        long sum = 0;

        // Add every recorded byte value to the total.
        for (long bytes : list) {
            sum += bytes;
        }

        return sum;
    }

    /**
     * Detects repeated access attempts to commonly sensitive ports.
     *
     * @param logs list of network logs to analyze
     * @return anomalies representing repeated sensitive-port access
     */
    public List<Anomaly> detectSensitivePorts(List<NetworkLog> logs) {

        // Maps each source -> destination pair to a map containing
        // each sensitive port and the number of times it was accessed.
        Map<String, Map<Integer, Integer>> map = new HashMap<>();

        // Stores detected sensitive-port anomalies.
        List<Anomaly> anomalies = new ArrayList<>();

        String type = "Sensitive Port Access";

        // Ports commonly associated with services that may require
        // additional monitoring.
        Set<Integer> sensitivePorts = Set.of(21, 22, 23, 3389);

        // Process every network log.
        for (NetworkLog log : logs) {

            // Create a unique source -> destination key.
            String key = log.getSourceIp() + "->" + log.getDestinationIp();

            int port = log.getPort();

            // Only track ports defined as sensitive.
            if (sensitivePorts.contains(port)) {

                // Get the port-count map for this connection pair.
                Map<Integer, Integer> portMap = map.get(key);

                // Create a new map when this pair is encountered
                // for the first time.
                if (portMap == null) {
                    portMap = new HashMap<>();
                }

                // Increment the access count for the current port.
                portMap.put(
                        port,
                        portMap.getOrDefault(port, 0) + 1
                );

                // Store the updated port map for this connection pair.
                map.put(key, portMap);
            }
        }

        // Examine every source -> destination pair.
        for (String key : map.keySet()) {

            // Split the connection key back into source and destination IPs.
            String[] split = key.split("->");

            String sourceIp = split[0];
            String destinationIp = split[1];

            // Check each sensitive port associated with this pair.
            for (int portKey : map.get(key).keySet()) {

                int portCount = map.get(key).get(portKey);

                // Generate an anomaly when the same sensitive port
                // has been accessed at least three times.
                if (portCount >= 3) {

                    // Describe how many access attempts were detected.
                    String description =
                            portCount + " access attempts detected";

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