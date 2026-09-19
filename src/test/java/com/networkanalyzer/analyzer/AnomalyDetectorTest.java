package com.networkanalyzer.analyzer;

import com.networkanalyzer.model.Anomaly;
import com.networkanalyzer.model.NetworkLog;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnomalyDetectorTest {

    @Test
    void shouldDetectRepeatedFailedAttempts() {
        List<NetworkLog> logs = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            logs.add(new NetworkLog(new String[]{
                    "2026-09-19T10:0" + i + ":00",
                    "192.168.1.10",
                    "192.168.1.20",
                    "TCP",
                    "22",
                    "FAILED",
                    "1000"
            }));
        }

        AnomalyDetector detector = new AnomalyDetector();

        List<Anomaly> anomalies = detector.detectAnomalies(logs);

        assertFalse(anomalies.isEmpty());

        assertTrue(
                anomalies.stream()
                        .anyMatch(anomaly ->
                                anomaly.getType()
                                        .equals("Repeated failed attempts")
        ));
    }

    @Test
    void shouldNotDetectRepeatedFailedAttemptsBelowThreshold() {
        List<NetworkLog> logs = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            logs.add(new NetworkLog(new String[]{
                    "2026-09-19T10:0" + i + ":00",
                    "192.168.1.10",
                    "192.168.1.20",
                    "TCP",
                    "22",
                    "FAILED",
                    "1000"
            }));
        }

        AnomalyDetector detector = new AnomalyDetector();

        List<Anomaly> anomalies = detector.detectAnomalies(logs);

        assertFalse(
                anomalies.stream()
                        .anyMatch(anomaly ->
                                anomaly.getType()
                                        .equals("Repeated failed attempts")
                        )
        );
    }

    @Test
    void shouldDetectPortScan() {
        List<NetworkLog> logs = new ArrayList<>();

        for (int port : new int[]{21, 22, 23, 80, 443}) {
            logs.add(new NetworkLog(new String[]{
                    "2026-09-19T10:00:00",
                    "192.168.1.10",
                    "192.168.1.20",
                    "TCP",
                    String.valueOf(port),
                    "SUCCESS",
                    "1000"
            }));
        }

        AnomalyDetector detector = new AnomalyDetector();

        List<Anomaly> anomalies = detector.detectAnomalies(logs);

        assertTrue(
                anomalies.stream()
                        .anyMatch(anomaly ->
                                anomaly.getType().equals("Port Scan"))
        );
    }

    @Test
    void shouldDetectHighDataTransfer() {
        List<NetworkLog> logs = new ArrayList<>();

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "600000"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "443",
                "SUCCESS",
                "600000"
        }));

        AnomalyDetector detector = new AnomalyDetector();

        List<Anomaly> anomalies = detector.detectAnomalies(logs);

        assertTrue(
                anomalies.stream()
                        .anyMatch(anomaly ->
                                anomaly.getType().equals("High Data Transfer"))
        );
    }

    @Test
    void shouldDetectSensitivePortAccess() {
        List<NetworkLog> logs = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            logs.add(new NetworkLog(new String[]{
                    "2026-09-19T10:0" + i + ":00",
                    "192.168.1.10",
                    "192.168.1.20",
                    "TCP",
                    "22",
                    "SUCCESS",
                    "1000"
            }));
        }

        AnomalyDetector detector = new AnomalyDetector();

        List<Anomaly> anomalies = detector.detectAnomalies(logs);


        assertTrue(
                anomalies.stream()
                        .anyMatch(anomaly ->
                                anomaly.getType().equals("Sensitive Port Access"))
        );
    }

    @Test
    void shouldNotDetectSensitivePortAccessBelowThreshold() {
        List<NetworkLog> logs = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            logs.add(new NetworkLog(new String[]{
                    "2026-09-19T10:0" + i + ":00",
                    "192.168.1.10",
                    "192.168.1.20",
                    "TCP",
                    "22",
                    "SUCCESS",
                    "1000"
            }));
        }

        AnomalyDetector detector = new AnomalyDetector();

        List<Anomaly> anomalies = detector.detectAnomalies(logs);

        assertFalse(
                anomalies.stream()
                        .anyMatch(anomaly ->
                                anomaly.getType().equals("Sensitive Port Access"))
        );
    }

}