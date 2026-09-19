# Network Log Analyzer

A Java-based network log analysis system designed to parse, analyze, filter, sort, and detect suspicious patterns in network logs.

The project focuses on data processing, algorithms, rule-based anomaly detection, input validation, and automated unit testing.

---

## Features

- Network log parsing and validation
- Log analysis and statistics
- Log filtering
- Log sorting using Quick Sort
- Rule-based anomaly detection
- Console-based CLI
- Input validation
- Malformed log handling
- Empty-log handling
- JUnit unit testing

### Anomaly Detection

The project currently detects:

1. Repeated failed connection attempts
2. Port scans
3. High data transfer
4. Sensitive port access

---

## Project Structure

```text
Network-Log-Analyzer/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── networkanalyzer/
│   │               ├── analyzer/
│   │               │   ├── AnomalyDetector.java
│   │               │   └── LogAnalyzer.java
│   │               │
│   │               ├── cli/
│   │               │   ├── ConsoleCLI.java
│   │               │   ├── FilterCLI.java
│   │               │   └── SortCLI.java
│   │               │
│   │               ├── filter/
│   │               │   └── LogFilter.java
│   │               │
│   │               ├── model/
│   │               │   ├── AnalysisResult.java
│   │               │   ├── Anomaly.java
│   │               │   └── NetworkLog.java
│   │               │
│   │               ├── parser/
│   │               │   └── LogParser.java
│   │               │
│   │               └── sort/
│   │                   └── LogSorter.java
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── networkanalyzer/
│                   ├── analyzer/
│                   │   ├── AnomalyDetectorTest.java
│                   │   └── LogAnalyzerTest.java
│                   │
│                   ├── filter/
│                   │   └── LogFilterTest.java
│                   │
│                   ├── model/
│                   │   └── NetworkLogTest.java
│                   │
│                   ├── parser/
│                   │   └── LogParserTest.java
│                   │
│                   └── sort/
│                       └── LogSorterTest.java
│
├── logs/
│   └── sample.log
│
├── pom.xml
└── README.md
```

---

# Log Format

Each log entry contains seven fields:

```text
Timestamp SourceIP DestinationIP Protocol Port Status Bytes
```

Example:

```text
2026-09-19T10:01 192.168.1.10 192.168.1.20 TCP 443 SUCCESS 1200
```

### Fields

| Field | Description |
|---|---|
| Timestamp | Date and time of the network event |
| Source IP | IP address initiating the connection |
| Destination IP | IP address receiving the connection |
| Protocol | Network protocol |
| Port | Destination port |
| Status | Connection status |
| Bytes | Number of bytes transferred |

---

# 1. Log Parsing

The `LogParser` reads network logs from `logs/sample.log` and converts valid entries into `NetworkLog` objects.

The parser handles malformed entries without stopping the entire parsing process.

### Validation

The application validates:

- Timestamp format
- Number of fields
- Protocol
- Port range
- Numeric port values
- Connection status
- Byte values

Invalid records are skipped and reported instead of terminating the application.

---

# 2. Log Analysis

The `LogAnalyzer` generates statistics from the parsed logs.

Currently supported statistics include:

- Total number of logs
- Successful connections
- Failed connections
- Protocol distribution
- Port distribution
- Source IP distribution

The statistics are calculated dynamically from the loaded log file.

---

# 3. Log Filtering

Logs can be filtered using five criteria:

```text
1. Source IP
2. Destination IP
3. Protocol
4. Port
5. Status
```

Example:

```text
Enter filter option:
1. Source IP
2. Destination IP
3. Protocol
4. Port
5. Status
```

The filtering logic is implemented in the `LogFilter` class, while user interaction and input validation are handled by `FilterCLI`.

Supported status values:

```text
SUCCESS
FAILED
```

Supported protocols:

```text
TCP
UDP
```

---

# 4. Log Sorting

The project implements **Quick Sort** for sorting network logs.

Logs can currently be sorted by:

- Timestamp
- Bytes
- Port
- Status

Both ascending and descending orders are supported.

The sorting implementation uses Java's `Comparator` interface, allowing the same Quick Sort implementation to work with different log fields.

### Sorting Algorithm

The project uses **Hoare Partition Quick Sort**.

The sorting operation is performed in-place on the list.

---

# 5. Rule-Based Anomaly Detection

The project contains four rule-based anomaly detection techniques.

## 5.1 Repeated Failed Attempts

Detects repeated failed connections between the same source and destination IP addresses.

### Rule

A source-destination pair is considered anomalous when it has:

```text
3 or more FAILED attempts
```

The implementation groups failed attempts using a map:

```text
Map<String, Integer>
```

The key represents:

```text
Source IP -> Destination IP
```

---

## 5.2 Port Scan Detection

Detects when a source IP contacts a destination IP using multiple different ports.

### Rule

A source-destination pair is flagged when it accesses:

```text
5 or more unique ports
```

The implementation uses:

```text
Map<String, Set<Integer>>
```

A `Set` is used so that repeated access to the same port is counted only once.

---

## 5.3 High Data Transfer

Detects unusually large amounts of data transferred between the same source and destination.

### Rule

A source-destination pair is flagged when the total transferred data reaches:

```text
1,000,000 bytes
```

The implementation groups byte values using:

```text
Map<String, List<Long>>
```

The byte values are summed for each source-destination pair.

---

## 5.4 Sensitive Port Access

Detects repeated access to commonly sensitive ports.

Currently monitored ports are:

```text
21
22
23
3389
```

### Rule

A source-destination-port combination is flagged when the same sensitive port is accessed:

```text
3 or more times
```

Both successful and failed connections are considered.

---

# Anomaly Detection Architecture

The anomaly detection system is organized into separate detection methods.

```text
detectAnomalies()
       │
       ├── detectRepeatedFailedAttempts()
       │
       ├── detectPortScans()
       │
       ├── detectHighBytes()
       │
       └── detectSensitivePortAccess()
       │
       ↓
   List<Anomaly>
```

This structure makes it easier to add new detection rules in the future.

---

# 6. Console CLI

The application provides an interactive console interface.

### Main Menu

```text
1. View Log Analysis
2. Filter Logs
3. Sort Logs
4. View All Logs
5. Exit
```

The CLI handles:

- Invalid menu input
- Invalid numeric input
- Invalid filter values
- Invalid sorting options
- Empty log files
- Navigation between menus

---

# 7. Unit Testing

The project includes JUnit tests for the main components.

### Tested Components

- `NetworkLog`
- `LogParser`
- `LogFilter`
- `LogSorter`
- `AnomalyDetector`
- `LogAnalyzer`

### Test Coverage Includes

- Valid log creation
- Invalid timestamps
- Invalid protocols
- Invalid ports
- Negative ports
- Invalid statuses
- Negative byte values
- Malformed log entries
- Source IP filtering
- Destination IP filtering
- Protocol filtering
- Port filtering
- Status filtering
- Timestamp sorting
- Byte sorting
- Port sorting
- Status sorting
- Repeated failed attempt detection
- Port scan detection
- High data transfer detection
- Sensitive port detection
- Threshold validation
- Log analysis
- Empty input handling

The complete test suite currently passes successfully.

---

# Technologies Used

- **Java**
- **Maven**
- **JUnit**
- **Lombok**
- **Apache Commons Validator**
- **Java Collections Framework**
- **Object-Oriented Programming**

---

# Algorithms & Data Structures

| Feature | Algorithm / Data Structure |
|---|---|
| Log Storage | `ArrayList` |
| Log Analysis | `HashMap` |
| Filtering | Linear Search |
| Sorting | Quick Sort |
| Sorting Comparison | `Comparator` |
| Failed Attempt Detection | `HashMap` |
| Port Scan Detection | `HashMap + HashSet` |
| High Data Transfer | `HashMap + ArrayList` |
| Sensitive Port Detection | `HashMap` |

---

# Project Status

## Completed

- [x] Network log model
- [x] Log parser
- [x] Parser input validation
- [x] Malformed log handling
- [x] Log analysis
- [x] Log filtering
- [x] Quick Sort implementation
- [x] Timestamp sorting
- [x] Byte sorting
- [x] Port sorting
- [x] Status sorting
- [x] Console CLI
- [x] CLI input validation
- [x] Empty-log handling
- [x] Repeated failed attempt detection
- [x] Port scan detection
- [x] High data transfer detection
- [x] Sensitive port access detection
- [x] JUnit unit tests

---

# Future Development

The project is planned to be extended with more advanced anomaly detection and analysis capabilities.

- [ ] Additional anomaly detection rules
- [ ] Configurable anomaly thresholds
- [ ] Time-window based anomaly detection
- [ ] Statistical anomaly detection
- [ ] Machine learning based anomaly detection
- [ ] Improved anomaly reporting
- [ ] Visualization and dashboards
- [ ] Large-scale log processing

---

# Future Machine Learning Integration

The current anomaly detection system is rule-based.

Future versions can use the existing log-processing pipeline as the foundation for machine learning.

Potential features for ML models include:

- Connection frequency
- Failed connection count
- Unique ports contacted
- Bytes transferred
- Connection status
- Protocol
- Source-destination behavior
- Time-based activity patterns

This can allow the project to move from manually defined rules toward data-driven anomaly detection.

---

# Project Architecture

```text
                    Network Logs
                         │
                         ▼
                    LogParser
                         │
                         ▼
                   NetworkLog
                         │
             ┌───────────┼───────────┐
             │           │           │
             ▼           ▼           ▼
        LogAnalyzer   LogFilter   LogSorter
             │           │           │
             └───────────┼───────────┘
                         │
                         ▼
                 AnomalyDetector
                         │
          ┌──────────────┼──────────────┐
          │              │              │
          ▼              ▼              ▼
      Failed          Port Scan     High Data
      Attempts                       Transfer
                         │
                         ▼
                  Sensitive Ports
                         │
                         ▼
                      Anomaly
```

---

# Running the Project

### Requirements

- Java Development Kit
- Maven
- IntelliJ IDEA or another Java IDE

### Clone the Repository

```bash
git clone https://github.com/Md-Afzaal/Network-Log-Analyzer.git
```

### Open the Project

Open the project in IntelliJ IDEA and allow Maven to load the dependencies.

### Run

Run:

```text
Main.java
```

The application will start the console interface.

---

# Testing

Run the JUnit test suite from IntelliJ IDEA or Maven.

The tests are located under:

```text
src/test/java/
```

The test suite validates the core parsing, filtering, sorting, analysis, and anomaly detection functionality.

---

# Author

**Aish**

Java-based network log analysis project focused on:

- Data processing
- Algorithms
- Network log analysis
- Rule-based anomaly detection
- Input validation
- Automated testing

---

## Repository

GitHub:

https://github.com/Md-Afzaal/Network-Log-Analyzer.git
