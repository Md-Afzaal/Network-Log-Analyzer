package com.networkanalyzer.sort;

import com.networkanalyzer.model.NetworkLog;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogSorterTest {
    @Test
    void shouldSortByBytesAscending() {
        NetworkLog log1 = new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "5000"
        });

        NetworkLog log2 = new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.11",
                "192.168.1.20",
                "TCP",
                "443",
                "SUCCESS",
                "1000"
        });

        NetworkLog log3 = new NetworkLog(new String[]{
                "2026-09-19T10:02:00",
                "192.168.1.12",
                "192.168.1.20",
                "UDP",
                "53",
                "FAILED",
                "3000"
        });

        List<NetworkLog> logs = new ArrayList<>(List.of(log1, log2, log3));

        LogSorter sorter = new LogSorter();

        sorter.quickSort(
                logs,
                0,
                logs.size() - 1,
                Comparator.comparingLong(NetworkLog::getBytes)
        );

        assertEquals(1000, logs.get(0).getBytes());
        assertEquals(3000, logs.get(1).getBytes());
        assertEquals(5000, logs.get(2).getBytes());
    }

    @Test
    void shouldSortByBytesDescending() {
        NetworkLog log1 = new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "5000"
        });

        NetworkLog log2 = new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.11",
                "192.168.1.20",
                "TCP",
                "443",
                "SUCCESS",
                "1000"
        });

        NetworkLog log3 = new NetworkLog(new String[]{
                "2026-09-19T10:02:00",
                "192.168.1.12",
                "192.168.1.20",
                "UDP",
                "53",
                "FAILED",
                "3000"
        });

        List<NetworkLog> logs = new ArrayList<>(List.of(log1, log2, log3));

        LogSorter sorter = new LogSorter();

        sorter.quickSort(
                logs,
                0,
                logs.size() - 1,
                Comparator.comparingLong(NetworkLog::getBytes)
        );

        assertEquals(5000, logs.get(2).getBytes());
        assertEquals(3000, logs.get(1).getBytes());
        assertEquals(1000, logs.get(0).getBytes());
    }

    @Test
    void shouldSortByTimestampAscending() {
        NetworkLog log1 = new NetworkLog(new String[]{
                "2026-09-19T10:05:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1000"
        });

        NetworkLog log2 = new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.11",
                "192.168.1.20",
                "TCP",
                "443",
                "SUCCESS",
                "2000"
        });

        NetworkLog log3 = new NetworkLog(new String[]{
                "2026-09-19T10:03:00",
                "192.168.1.12",
                "192.168.1.20",
                "UDP",
                "53",
                "FAILED",
                "3000"
        });

        List<NetworkLog> logs =
                new ArrayList<>(List.of(log1, log2, log3));

        LogSorter sorter = new LogSorter();

        sorter.quickSort(
                logs,
                0,
                logs.size() - 1,
                Comparator.comparing(NetworkLog::getTimestamp)
        );

        assertEquals(
                LocalDateTime.parse("2026-09-19T10:01:00"),
                logs.get(0).getTimestamp()
        );

        assertEquals(
                LocalDateTime.parse("2026-09-19T10:03:00"),
                logs.get(1).getTimestamp()
        );

        assertEquals(
                LocalDateTime.parse("2026-09-19T10:05:00"),
                logs.get(2).getTimestamp()
        );
    }

    @Test
    void shouldSortByPortAscending() {
        NetworkLog log1 = new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "443",
                "SUCCESS",
                "1000"
        });

        NetworkLog log2 = new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.11",
                "192.168.1.20",
                "TCP",
                "22",
                "SUCCESS",
                "2000"
        });

        NetworkLog log3 = new NetworkLog(new String[]{
                "2026-09-19T10:02:00",
                "192.168.1.12",
                "192.168.1.20",
                "UDP",
                "80",
                "FAILED",
                "3000"
        });

        List<NetworkLog> logs =
                new ArrayList<>(List.of(log1, log2, log3));

        LogSorter sorter = new LogSorter();

        sorter.quickSort(
                logs,
                0,
                logs.size() - 1,
                Comparator.comparingInt(NetworkLog::getPort)
        );

        assertEquals(22, logs.get(0).getPort());
        assertEquals(80, logs.get(1).getPort());
        assertEquals(443, logs.get(2).getPort());
    }

    @Test
    void shouldSortByStatusFailedFirst() {
        NetworkLog log1 = new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1000"
        });

        NetworkLog log2 = new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.11",
                "192.168.1.20",
                "TCP",
                "443",
                "FAILED",
                "2000"
        });

        NetworkLog log3 = new NetworkLog(new String[]{
                "2026-09-19T10:02:00",
                "192.168.1.12",
                "192.168.1.20",
                "UDP",
                "53",
                "SUCCESS",
                "3000"
        });

        List<NetworkLog> logs =
                new ArrayList<>(List.of(log1, log2, log3));

        LogSorter sorter = new LogSorter();

        sorter.quickSort(
                logs,
                0,
                logs.size() - 1,
                Comparator.comparing(NetworkLog::getStatus)
        );

        assertEquals("FAILED", logs.get(0).getStatus());
        assertEquals("SUCCESS", logs.get(1).getStatus());
        assertEquals("SUCCESS", logs.get(2).getStatus());
    }

    @Test
    void shouldSortByStatusSuccessfulFirst() {
        NetworkLog log1 = new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "FAILED",
                "1000"
        });

        NetworkLog log2 = new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.11",
                "192.168.1.20",
                "TCP",
                "443",
                "SUCCESS",
                "2000"
        });

        NetworkLog log3 = new NetworkLog(new String[]{
                "2026-09-19T10:02:00",
                "192.168.1.12",
                "192.168.1.20",
                "UDP",
                "53",
                "FAILED",
                "3000"
        });

        List<NetworkLog> logs =
                new ArrayList<>(List.of(log1, log2, log3));

        LogSorter sorter = new LogSorter();

        sorter.quickSort(
                logs,
                0,
                logs.size() - 1,
                Comparator.comparing(NetworkLog::getStatus)
        );

        // Read the sorted list backwards for successful-first order
        assertEquals("SUCCESS", logs.get(2).getStatus());
        assertEquals("FAILED", logs.get(1).getStatus());
        assertEquals("FAILED", logs.get(0).getStatus());
    }

}
