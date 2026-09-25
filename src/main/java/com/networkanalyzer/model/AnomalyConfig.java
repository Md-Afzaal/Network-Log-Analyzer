package com.networkanalyzer.model;

import lombok.Data;

@Data
public class AnomalyConfig {
    private int failedAttemptThreshold;
    private long timeWindowMinutes;
    private int portScanThreshold;
    private long highDataTransferThreshold;
    private int sensitivePortThreshold;

    public AnomalyConfig(
            int failedAttemptThreshold,
            long timeWindowMinutes,
            int portScanThreshold,
            long highDataTransferThreshold,
            int sensitivePortThreshold
    ){
        if (failedAttemptThreshold <= 0) {
            throw new IllegalArgumentException(
                    "Failed attempt threshold must be greater than 0"
            );
        }

        if (timeWindowMinutes <= 0) {
            throw new IllegalArgumentException(
                    "Time window must be greater than 0"
            );
        }

        if (portScanThreshold <= 0) {
            throw new IllegalArgumentException(
                    "Port scan threshold must be greater than 0"
            );
        }

        if (highDataTransferThreshold <= 0) {
            throw new IllegalArgumentException(
                    "High data transfer threshold must be greater than 0"
            );
        }

        if (sensitivePortThreshold <= 0) {
            throw new IllegalArgumentException(
                    "Sensitive port threshold must be greater than 0"
            );
        }

        this.failedAttemptThreshold = failedAttemptThreshold;
        this.timeWindowMinutes = timeWindowMinutes;
        this.portScanThreshold = portScanThreshold;
        this.highDataTransferThreshold = highDataTransferThreshold;
        this.sensitivePortThreshold = sensitivePortThreshold;
    }

}
