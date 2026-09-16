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
            this.protocol = lst[3].toUpperCase();
            validateProtocol(this.protocol);
            this.port = Integer.parseInt(lst[4]);
            validatePort(this.port);
            this.status = lst[5].toUpperCase();
            validateStatus(this.status);
            this.bytes = Long.parseLong(lst[6]);
            validateBytes(this.bytes);
    }

    private void validatePort(int port){
        if(port>65535 || port<0){
            throw new IllegalArgumentException("Invalid port: "+ port);
        }
    }

    private void validateBytes(long bytes){
        if(bytes<0){
            throw new IllegalArgumentException("Invalid bytes: "+ bytes);
        }
    }

    private void validateStatus(String status){
        if(!(status.equals("SUCCESS") || status.equals("FAILED"))){
            throw new IllegalArgumentException("Invalid status: "+ status);
        }
    }

    private void validateProtocol(String protocol){
        if(!(protocol.equals("TCP") || protocol.equals("UDP"))){
            throw new IllegalArgumentException("Invalid protocol: "+ protocol);
        }
    }
}
