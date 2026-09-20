package com.networkanalyzer.integration;

import com.networkanalyzer.analyzer.AnomalyDetector;
import com.networkanalyzer.analyzer.LogAnalyzer;
import com.networkanalyzer.filter.LogFilter;
import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.model.Anomaly;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;
import com.networkanalyzer.sort.LogSorter;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkLogAnalyzerIntegrationTest {
    @Test
    void shouldProcessParsedLogsEndToEnd() {
        LogParser parser = new LogParser();
        LogAnalyzer analyzer = new LogAnalyzer();
        LogFilter filter = new LogFilter();
        LogSorter sorter = new LogSorter();
        AnomalyDetector detector = new AnomalyDetector();

        List<NetworkLog> logs = parser.parse();

        assertNotNull(logs);
        assertFalse(logs.isEmpty());

        // Analyze
        AnalysisResult result = analyzer.analyze(logs);

        assertNotNull(result);
        assertEquals(logs.size(), result.getTotalLogs());

        // Detect anomalies
        String type = "High Data Transfer";
        long threshold = 1_000_000L;

        List<Anomaly> anomalies = detector.detectHighBytes(logs);

        assertNotNull(anomalies);

        for (Anomaly anomaly : anomalies) {
            assertEquals(type, anomaly.getType());
            assertTrue(anomaly.getCount() >= threshold);
        }

        // Filter
        int choice = 3;
        String target = "TCP";

        List<NetworkLog> filteredLogs = filter.filter(logs, choice, target);

        assertNotNull(filteredLogs);
        assertFalse(filteredLogs.isEmpty());

        for (NetworkLog log : filteredLogs) {
            assertEquals(target, log.getProtocol());
        }

        // Sort
        int low = 0;
        int high = logs.size() - 1;

        sorter.quickSort(
                logs,
                low,
                high,
                Comparator.comparingLong(NetworkLog::getBytes)
        );

        for (int i = 0; i < logs.size() - 1; i++) {
            assertTrue(
                    logs.get(i).getBytes() <= logs.get(i + 1).getBytes()
            );
        }
    }

}
