package com.networkanalyzer.integration;

import com.networkanalyzer.analyzer.LogAnalyzer;
import com.networkanalyzer.model.AnalysisResult;
import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParserAnalyzerIntegrationTest {

    @Test
    void shouldAnalyzeParsedLogs() {
        LogParser parser = new LogParser();
        LogAnalyzer logAnalyzer = new LogAnalyzer();

        List<NetworkLog> logs = parser.parse();
        AnalysisResult result = logAnalyzer.analyze(logs);

        assertNotNull(logs);
        assertNotNull(result);
        assertEquals(logs.size(),result.getTotalLogs());
    }
}
