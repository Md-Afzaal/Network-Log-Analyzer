package com.networkanalyzer.integration;

import com.networkanalyzer.analyzer.AnomalyDetector;
import com.networkanalyzer.model.Anomaly;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;
import com.networkanalyzer.sort.LogSorter;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ParserAnomalyIntegrationTest {

    @Test
    void shouldDetectAnomaliesFromParsedLogs() {

        String type = "High Data Transfer";
        long threshold = 1_000_000L;
        LogParser parser = new LogParser();
        AnomalyDetector anomalyDetector = new AnomalyDetector();

        List<NetworkLog> logs = parser.parse();
        List<Anomaly> anomalies = anomalyDetector.detectHighBytes(logs);


        assertNotNull(logs);
        assertNotNull(anomalies);

        for (Anomaly log: anomalies){
            assertEquals(type.toUpperCase(),log.getType().toUpperCase());
            assertTrue(log.getCount()>=threshold);
        }
    }
}
