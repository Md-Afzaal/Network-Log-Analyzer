package com.networkanalyzer.filter;

import com.networkanalyzer.model.NetworkLog;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogFilterTest {

    @Test
    void shouldFilterBySourceIp() {
        NetworkLog log1 = new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1200"
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

        LogFilter filter = new LogFilter();

        List<NetworkLog> logs = List.of(log1, log2);

        List<NetworkLog> result =
                filter.filter(logs, 1, "192.168.1.10");

        assertEquals(1, result.size());
        assertEquals("192.168.1.10", result.get(0).getSourceIp());
    }

    @Test
    void shouldFilterByDestinationIp() {
        NetworkLog log1 = new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1200"
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

        LogFilter filter = new LogFilter();

        List<NetworkLog> logs = List.of(log1, log2);

        List<NetworkLog> result =
                filter.filter(logs, 2, "192.168.1.20");

        assertEquals(2, result.size());
        assertEquals("192.168.1.20", result.get(0).getDestinationIp());
        assertEquals("192.168.1.20", result.get(1).getDestinationIp());
    }

    @Test
    void shouldFilterByProtocol() {
        NetworkLog log1 = new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1200"
        });

        NetworkLog log2 = new NetworkLog(new String[]{
                "2026-09-19T10:01:00",
                "192.168.1.11",
                "192.168.1.20",
                "UDP",
                "53",
                "FAILED",
                "2000"
        });

        LogFilter filter = new LogFilter();

        List<NetworkLog> logs = List.of(log1, log2);

        List<NetworkLog> result =
                filter.filter(logs, 3, "TCP");

        assertEquals(1, result.size());
        assertEquals("TCP", result.get(0).getProtocol());
    }

    @Test
    void shouldFilterByPort() {
        NetworkLog log1 = new NetworkLog(new String[]{
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1200"
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

        LogFilter filter = new LogFilter();

        List<NetworkLog> logs = List.of(log1, log2);

        List<NetworkLog> result =
                filter.filter(logs, 4, "80");

        assertEquals(1, result.size());
        assertEquals(80, result.get(0).getPort());
    }


}