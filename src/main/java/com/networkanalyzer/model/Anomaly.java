package com.networkanalyzer.model;

import lombok.Data;

@Data
public class Anomaly {
    private String type;
    private String sourceIp;
    private String destinationIp;
    private String description;
    private int count;

    public Anomaly(String type, String sourceIp, String destinationIp, String description, int count) {
        this.type = type;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.description = description;
        this.count = count;
    }
}
