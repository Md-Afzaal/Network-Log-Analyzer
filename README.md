# Network Log Analyzer

A Java-based network log analysis system designed to parse, analyze, filter, sort, and detect suspicious patterns in network logs.

## Current Features

- Network log parsing
- Log analysis and statistics
- Log filtering
- Log sorting using Quick Sort
- Rule-based anomaly detection
  - Repeated failed connection attempts
  - Port scan detection
  - High data transfer detection
- Console-based CLI

## Project Structure

```text
Network-Log-Analyzer/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── networkanalyzer/
│                   ├── analyzer/
│                   │   ├── AnomalyDetector.java
│                   │   └── LogAnalyzer.java
│                   │
│                   ├── cli/
│                   │   ├── ConsoleCLI.java
│                   │   ├── FilterCLI.java
│                   │   └── SortCLI.java
│                   │
│                   ├── filter/
│                   │   └── LogFilter.java
│                   │
│                   ├── model/
│                   │   ├── AnalysisResult.java
│                   │   ├── Anomaly.java
│                   │   └── NetworkLog.java
│                   │
│                   ├── parser/
│                   │   └── LogParser.java
│                   │
│                   └── sort/
│                       └── LogSorter.java
│
├── logs/
│   └── sample.log
│
├── pom.xml
└── README.md
```

## Features

### 1. Network Log Parsing

The application reads network logs from a log file and converts each entry into a `NetworkLog` object.

Each network log contains:

- Timestamp
- Source IP
- Destination IP
- Protocol
- Port
- Status
- Bytes transferred

Example:

```text
2026-08-20T10:15:30 192.168.1.10 192.168.1.50 TCP 443 SUCCESS 1200
```

The parser processes the log file and stores the resulting entries in a list.

### 2. Log Analysis

The analyzer generates statistics from the parsed logs.

Currently, it provides:

- Total number of logs
- Successful connections
- Failed connections
- Protocol distribution
- Port distribution
- Source IP distribution

Example:

```text
Total Logs: 50
Successful Connections: 36
Failed Connections: 14

Protocol Distribution:
TCP: 37
UDP: 13
```

### 3. Log Filtering

Logs can be filtered using different criteria:

- Source IP
- Destination IP
- Protocol
- Port
- Status

The filtering functionality is implemented using a dedicated `LogFilter` class.

### 4. Log Sorting

The project implements Quick Sort for sorting network logs.

Users can sort logs by:

- Timestamp
- Bytes
- Port
- Status

Ascending and descending ordering are supported.

The sorting implementation uses Java's `Comparator` interface, allowing the same Quick Sort implementation to work with different log fields.

Example:

```text
Timestamp:
Oldest → Newest
Newest → Oldest

Bytes:
Smallest → Largest
Largest → Smallest
```

### 5. Rule-Based Anomaly Detection

The project currently includes three rule-based anomaly detection techniques.

#### 5.1 Repeated Failed Attempts

Detects repeated failed connections between the same source and destination IP addresses.

The current rule flags a connection pair when it has 3 or more failed attempts.

The implementation uses:

```text
Map<String, Integer>
```

where the key represents:

```text
source IP -> destination IP
```

and the value represents the number of failed attempts.

Example:

```text
Source: 192.168.1.20
Destination: 192.168.1.50
Failed Attempts: 9
```

Result:

```text
Type: Repeated failed attempts
Description: 9 failed connection attempts detected
```

#### 5.2 Port Scan Detection

Detects when the same source IP contacts the same destination IP using multiple different ports.

The current rule flags a pair when it contacts 5 or more unique ports.

The implementation uses:

```text
Map<String, Set<Integer>>
```

The `Set` ensures that repeated connections to the same port are counted only once.

Example:

```text
Source: 192.168.1.20
Destination: 192.168.1.50

Ports:
21
22
23
25
53
80
110
443
8080
```

Result:

```text
Type: Port Scan
Description: 9 unique ports connected
```

#### 5.3 High Data Transfer

Detects source-to-destination pairs that transfer a large amount of data.

The implementation groups byte values using:

```text
Map<String, List<Long>>
```

The byte values for each source → destination pair are collected and then summed.

The total is compared against a configurable threshold.

For testing with the current sample dataset, the threshold can be set to:

```text
10,000 bytes
```

Example:

```text
Source: 192.168.1.15
Destination: 142.250.195.14

Transferred:
8000 bytes
7500 bytes
11600 bytes

Total:
27100 bytes
```

Result:

```text
Type: High Data Transfer
Description: 27100 bytes transferred between source and destination
```

## Anomaly Detection Architecture

All anomaly detection rules are coordinated through the `detectAnomalies()` method.

```text
detectAnomalies()
       │
       ├── detectRepeatedFailedAttempts()
       │
       ├── detectPortScans()
       │
       └── detectHighBytes()
       │
       ↓
   List<Anomaly>
```

Each detection rule is implemented as a separate method, making it easier to add additional rules later.

## Console Interface

The application currently provides a console-based interface.

Main menu:

```text
1. View Log Analysis
2. Filter Logs
3. Sort Logs
4. View All Logs
5. Exit
```

Filtering and sorting functionality use separate CLI classes to keep the main console interface organized.

## Technologies Used

- Java
- Maven
- Lombok
- Java Collections Framework
- Object-Oriented Programming
- Quick Sort
- Rule-Based Anomaly Detection

## Algorithms and Data Structures

| Feature | Data Structure / Algorithm |
|---|---|
| Log Storage | `ArrayList` |
| Log Analysis | `HashMap` |
| Filtering | Linear Search |
| Sorting | Quick Sort |
| Timestamp Sorting | `Comparator` |
| Port Sorting | `Comparator` |
| Byte Sorting | `Comparator` |
| Status Sorting | `Comparator` |
| Failed Attempt Detection | `HashMap` |
| Port Scan Detection | `HashMap + HashSet` |
| High Data Transfer | `HashMap + ArrayList` |

## Future Development

Planned improvements include:

- Sensitive port access detection
- Additional rule-based anomaly detection
- Configurable thresholds
- Time-window based detection
- Statistical anomaly detection
- Machine learning based detection
- Improved anomaly reporting
- Visualization and dashboards

## Project Status

### Completed

- [x] Network log model
- [x] Log parser
- [x] Log analysis
- [x] Log filtering
- [x] Quick Sort implementation
- [x] Timestamp sorting
- [x] Byte sorting
- [x] Port sorting
- [x] Status sorting
- [x] Console CLI
- [x] Repeated failed attempt detection
- [x] Port scan detection
- [x] High data transfer detection

### In Progress

- [ ] Additional anomaly detection rules
- [ ] Advanced anomaly detection
- [ ] Machine learning based detection

## Author

**Aish**

Built as a Java-based network log analysis project focusing on data processing, algorithms, and anomaly detection.
