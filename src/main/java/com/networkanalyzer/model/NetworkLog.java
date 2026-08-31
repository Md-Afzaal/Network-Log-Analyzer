package com.networkanalyzer.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NetworkLog {
    private LocalDateTime timestamp;
    private String sourceIp;
    private String destinationIp;
    private String protocol;
    private int port;
    private String status;
    private long bytes;

    public NetworkLog(String[] lst) {
            this.timestamp = LocalDateTime.parse(lst[0]);
            this.sourceIp = lst[1];
            this.destinationIp = lst[2];
            this.protocol = lst[3];
            this.port = Integer.parseInt(lst[4]);
            this.status = lst[5];
            this.bytes = Long.parseLong(lst[6]);
    }

}
