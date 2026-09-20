package com.networkanalyzer.integration;

import com.networkanalyzer.model.NetworkLog;
import com.networkanalyzer.parser.LogParser;
import com.networkanalyzer.sort.LogSorter;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserSorterIntegrationTest {
    @Test
    void shouldSortParsedLogs() {

        LogParser parser = new LogParser();
        LogSorter logSorter = new LogSorter();

        List<NetworkLog> logs = parser.parse();

        int low = 0;
        int high = logs.size()-1;

        logSorter.quickSort(logs,low,high, Comparator.comparingLong(NetworkLog::getBytes));

        assertNotNull(logs);
        assertFalse(logs.isEmpty());

        for (int i=0;i< logs.size()-1;i++){
            assertTrue(logs.get(i).getBytes()<= logs.get(i+1).getBytes());
        }
    }
}
