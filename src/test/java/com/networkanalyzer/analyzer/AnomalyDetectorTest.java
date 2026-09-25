package com.networkanalyzer.analyzer;

import com.networkanalyzer.model.Anomaly;
import com.networkanalyzer.model.AnomalyConfig;
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

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

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

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

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

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

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
        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

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

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

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

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

        List<Anomaly> anomalies = detector.detectAnomalies(logs);

        assertFalse(
                anomalies.stream()
                        .anyMatch(anomaly ->
                                anomaly.getType().equals("Sensitive Port Access"))
        );
    }

    @Test
    void shouldDetectRepeatedFailedAttemptsWithinTimeWindow() {

        List<NetworkLog> logs = new ArrayList<>();

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "22",
                "FAILED",
                "100"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:02:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "22",
                "FAILED",
                "100"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:04:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "22",
                "FAILED",
                "100"
        }));

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

        List<Anomaly> anomalies =
                detector.detectRepeatedFailedAttempts(logs);

        assertNotNull(anomalies);
        assertEquals(1, anomalies.size());

        Anomaly anomaly = anomalies.get(0);

        assertEquals("Repeated failed attempts", anomaly.getType());
        assertEquals("192.168.1.10", anomaly.getSourceIp());
        assertEquals("192.168.1.20", anomaly.getDestinationIp());
        assertEquals(3, anomaly.getCount());
    }

    @Test
    void shouldRespectCustomPortScanThreshold() {
        List<NetworkLog> logs = new ArrayList<>();

        for (int port = 80; port < 85; port++) {
            logs.add(new NetworkLog(new String[]{
                    "2026-09-19T10:00:00",
                    "192.168.1.10",
                    "192.168.1.20",
                    "TCP",
                    String.valueOf(port),
                    "SUCCESS",
                    "100"
            }));
        }

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                6,          // Port scan threshold
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

        List<Anomaly> anomalies = detector.detectPortScans(logs);

        assertTrue(anomalies.isEmpty());
    }

    @Test
    void shouldDetectPortScanAtConfiguredThreshold() {
        List<NetworkLog> logs = new ArrayList<>();

        for (int port = 80; port < 85; port++) {
            logs.add(new NetworkLog(new String[]{
                    "2026-09-19T10:00:00",
                    "192.168.1.10",
                    "192.168.1.20",
                    "TCP",
                    String.valueOf(port),
                    "SUCCESS",
                    "100"
            }));
        }

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,          // Port scan threshold
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

        List<Anomaly> anomalies = detector.detectPortScans(logs);

        assertEquals(1, anomalies.size());
        assertEquals(5, anomalies.get(0).getCount());
    }

    @Test
    void shouldRespectCustomHighDataTransferThreshold() {
        List<NetworkLog> logs = new ArrayList<>();

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "500000"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "443",
                "SUCCESS",
                "500000"
        }));

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_001L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

        List<Anomaly> anomalies = detector.detectHighBytes(logs);

        assertTrue(anomalies.isEmpty());
    }

    @Test
    void shouldDetectHighDataTransferAtConfiguredThreshold() {
        List<NetworkLog> logs = new ArrayList<>();

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "500000"
        }));

        logs.add(new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "443",
                "SUCCESS",
                "500000"
        }));

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

        List<Anomaly> anomalies = detector.detectHighBytes(logs);

        assertEquals(1, anomalies.size());
        assertEquals(1_000_000L, anomalies.get(0).getCount());
    }

    @Test
    void shouldRespectCustomSensitivePortThreshold() {
        List<NetworkLog> logs = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            logs.add(new NetworkLog(new String[]{
                    "2026-09-19T10:0" + i + ":00",
                    "192.168.1.10",
                    "192.168.1.20",
                    "TCP",
                    "22",
                    "SUCCESS",
                    "100"
            }));
        }

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_000L,
                4
        );

        AnomalyDetector detector = new AnomalyDetector(config);

        List<Anomaly> anomalies = detector.detectSensitivePorts(logs);

        assertTrue(anomalies.isEmpty());
    }

    @Test
    void shouldDetectSensitivePortAccessAtConfiguredThreshold() {
        List<NetworkLog> logs = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            logs.add(new NetworkLog(new String[]{
                    "2026-09-19T10:0" + i + ":00",
                    "192.168.1.10",
                    "192.168.1.20",
                    "TCP",
                    "22",
                    "SUCCESS",
                    "100"
            }));
        }

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

        List<Anomaly> anomalies = detector.detectSensitivePorts(logs);

        assertEquals(1, anomalies.size());
        assertEquals(3, anomalies.get(0).getCount());
        assertEquals(22, anomalies.get(0).getPort());
    }

    @Test
    void shouldRespectCustomFailedAttemptThreshold() {
        List<NetworkLog> logs = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            logs.add(new NetworkLog(new String[]{
                    "2026-09-19T10:0" + i + ":00",
                    "192.168.1.10",
                    "192.168.1.20",
                    "TCP",
                    "22",
                    "FAILED",
                    "100"
            }));
        }

        AnomalyConfig config = new AnomalyConfig(
                4,
                5,
                5,
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

        List<Anomaly> anomalies =
                detector.detectRepeatedFailedAttempts(logs);

        assertTrue(anomalies.isEmpty());
    }

    @Test
    void shouldDetectFailedAttemptsAtConfiguredThreshold() {
        List<NetworkLog> logs = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            logs.add(new NetworkLog(new String[]{
                    "2026-09-19T10:0" + i + ":00",
                    "192.168.1.10",
                    "192.168.1.20",
                    "TCP",
                    "22",
                    "FAILED",
                    "100"
            }));
        }

        AnomalyConfig config = new AnomalyConfig(
                3,
                5,
                5,
                1_000_000L,
                3
        );

        AnomalyDetector detector = new AnomalyDetector(config);

        List<Anomaly> anomalies =
                detector.detectRepeatedFailedAttempts(logs);

        assertEquals(1, anomalies.size());
        assertEquals(3, anomalies.get(0).getCount());
    }

}