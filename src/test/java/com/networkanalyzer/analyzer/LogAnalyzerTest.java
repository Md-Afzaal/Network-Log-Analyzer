package com.networkanalyzer.analyzer;

import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.model.NetworkLog;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogAnalyzerTest {

    @Test
    void shouldAnalyzeLogCounts() {
        List<NetworkLog> logs = new ArrayList<>();

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1000"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.11",
                "192.168.1.20",
                "TCP",
                "443",
                "FAILED",
                "2000"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:02:00",
                "192.168.1.12",
                "192.168.1.20",
                "UDP",
                "53",
                "SUCCESS",
                "3000"
        }));

        LogAnalyzer analyzer = new LogAnalyzer();

        AnalysisResult result = analyzer.analyze(logs);

        assertEquals(3, result.getTotalLogs());
        assertEquals(2, result.getSuccessCount());
        assertEquals(1, result.getFailedCount());
    }

    @Test
    void shouldCountProtocols() {
        List<NetworkLog> logs = new ArrayList<>();

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1000"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.11",
                "192.168.1.20",
                "TCP",
                "443",
                "FAILED",
                "2000"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:02:00",
                "192.168.1.12",
                "192.168.1.20",
                "UDP",
                "53",
                "SUCCESS",
                "3000"
        }));

        LogAnalyzer analyzer = new LogAnalyzer();

        AnalysisResult result = analyzer.analyze(logs);

        assertEquals(2, result.getProtocol().get("TCP"));
        assertEquals(1, result.getProtocol().get("UDP"));
    }

    @Test
    void shouldCountPorts() {
        List<NetworkLog> logs = new ArrayList<>();

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1000"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.11",
                "192.168.1.20",
                "TCP",
                "80",
                "FAILED",
                "2000"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:02:00",
                "192.168.1.12",
                "192.168.1.20",
                "UDP",
                "53",
                "SUCCESS",
                "3000"
        }));

        LogAnalyzer analyzer = new LogAnalyzer();

        AnalysisResult result = analyzer.analyze(logs);

        assertEquals(2, result.getPorts().get(80));
        assertEquals(1, result.getPorts().get(53));
    }

    @Test
    void shouldCountSourceIps() {
        List<NetworkLog> logs = new ArrayList<>();

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1000"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "443",
                "FAILED",
                "2000"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:02:00",
                "192.168.1.11",
                "192.168.1.20",
                "UDP",
                "53",
                "SUCCESS",
                "3000"
        }));

        LogAnalyzer analyzer = new LogAnalyzer();

        AnalysisResult result = analyzer.analyze(logs);

        assertEquals(2, result.getSourceIp().get("192.168.1.10"));
        assertEquals(1, result.getSourceIp().get("192.168.1.11"));
    }

    @Test
    void shouldHandleEmptyLogs() {
        LogAnalyzer analyzer = new LogAnalyzer();

        AnalysisResult result = analyzer.analyze(new ArrayList<>());

        assertNotNull(result);
        assertEquals(0, result.getTotalLogs());
        assertEquals(0, result.getSuccessCount());
        assertEquals(0, result.getFailedCount());

        assertTrue(result.getProtocol().isEmpty());
        assertTrue(result.getPorts().isEmpty());
        assertTrue(result.getSourceIp().isEmpty());
    }
}