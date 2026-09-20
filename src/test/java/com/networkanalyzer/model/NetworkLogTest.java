package com.networkanalyzer.model;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

class NetworkLogTest {

    @Test
    void shouldCreateValidNetworkLog() {
        String[] data = {
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1200"
        };

        NetworkLog log = new NetworkLog(data);

        assertEquals("192.168.1.10", log.getSourceIp());
        assertEquals("192.168.1.20", log.getDestinationIp());
        assertEquals("TCP", log.getProtocol());
        assertEquals(80, log.getPort());
        assertEquals("SUCCESS", log.getStatus());
        assertEquals(1200, log.getBytes());
    }

    @Test
    void shouldRejectNegativeBytes() {
        String[] data = {
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "-500"
        };

        assertThrows(
                IllegalArgumentException.class,
                () -> new NetworkLog(data)
        );
    }

    @Test
    void shouldRejectPortAboveMaximum() {
        String[] data = {
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "65536",
                "SUCCESS",
                "1200"
        };

        assertThrows(IllegalArgumentException.class,
                () -> new NetworkLog(data));
    }

    @Test
    void shouldRejectNegativePort() {
        String[] data = {
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "-1",
                "SUCCESS",
                "1200"
        };

        assertThrows(IllegalArgumentException.class,
                () -> new NetworkLog(data));
    }

    @Test
    void shouldRejectNonNummericPort(){
        String[] data = {
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "eigtht",
                "SUCCESS",
                "1200"
        };
        assertThrows(NumberFormatException.class,
                () -> new NetworkLog(data));

    }

    @Test
    void shouldRejectInvalidStatus() {
        String[] data = {
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "ERROR",
                "1200"
        };

        assertThrows(IllegalArgumentException.class,
                () -> new NetworkLog(data));
    }

    @Test
    void shouldNormalizeStatusToUpperCase() {
        String[] data = {
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "success",
                "1200"
        };

        NetworkLog log = new NetworkLog(data);

        assertEquals("SUCCESS", log.getStatus());
    }

    @Test
    void shouldRejectInvalidProtocol() {
        String[] data = {
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "HTTP",
                "80",
                "SUCCESS",
                "1200"
        };

        assertThrows(IllegalArgumentException.class,
                () -> new NetworkLog(data));
    }

    @Test
    void shouldNormalizeProtocolToUpperCase() {
        String[] data = {
                "2026-09-19T10:00:00",
                "192.168.1.10",
                "192.168.1.20",
                "tcp",
                "80",
                "SUCCESS",
                "1200"
        };

        NetworkLog log = new NetworkLog(data);

        assertEquals("TCP", log.getProtocol());
    }

    @Test
    void shouldRejectInvalidTimestamp() {
        String[] data = {
                "2026-99-99T25:70:80",
                "192.168.1.10",
                "192.168.1.20",
                "TCP",
                "80",
                "SUCCESS",
                "1200"
        };

        assertThrows(DateTimeParseException.class,
                () -> new NetworkLog(data));
    }
}