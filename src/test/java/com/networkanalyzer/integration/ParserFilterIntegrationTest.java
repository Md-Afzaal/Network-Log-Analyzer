package com.networkanalyzer.integration;

import com.networkanalyzer.analyzer.LogAnalyzer;
import com.networkanalyzer.filter.LogFilter;
import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserFilterIntegrationTest {

    @Test
    void shouldFilteredParsedLogs() {
        int choice = 3;
        String target = "TCP";
        LogParser parser = new LogParser();
        LogFilter logFilter = new LogFilter();

        List<NetworkLog> logs = parser.parse();
        List<NetworkLog> filteredLogs = logFilter.filter(logs,choice,target);

        assertNotNull(logs);
        assertNotNull(filteredLogs);
        assertFalse(filteredLogs.isEmpty());

        for (NetworkLog log: filteredLogs){
            assertEquals(target,log.getProtocol());
        }
    }
}
