package com.networkanalyzer.model;

import lombok.Data;

@Data
public class Anomaly {
    private String type;
    private String sourceIp;
    private String destinationIp;
    private String description;
    private long count;

    public Anomaly(
            String type,
            String sourceIp,
            String destinationIp,
            String description,
            long count
    ) {
        this.type = type;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.description = description;
        this.count = count;
    }
}
