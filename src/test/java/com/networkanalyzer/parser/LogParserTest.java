package com.networkanalyzer.parser;

import com.networkanalyzer.model.NetworkLog;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogParserTest {

    @Test
    void shouldParseValidLogs() {
        LogParser parser = new LogParser();

        List<NetworkLog> logs = parser.parse();

        assertNotNull(logs);
        assertEquals(91, logs.size());
    }

    @Test
    void shouldParseFirstLogCorrectly() {
        LogParser parser = new LogParser();

        List<NetworkLog> logs = parser.parse();

        NetworkLog firstLog = logs.get(0);

        assertEquals("192.168.1.10", firstLog.getSourceIp());
        assertEquals("142.250.195.14", firstLog.getDestinationIp());
        assertEquals("TCP", firstLog.getProtocol());
        assertEquals(443, firstLog.getPort());
        assertEquals("SUCCESS", firstLog.getStatus());
        assertEquals(18420, firstLog.getBytes());
    }

    @Test
    void shouldSkipMalformedLogs() {
        LogParser parser = new LogParser();

        List<NetworkLog> logs = parser.parse();

        assertNotNull(logs);

        for (NetworkLog log : logs) {
            assertNotNull(log);
        }
    }
}