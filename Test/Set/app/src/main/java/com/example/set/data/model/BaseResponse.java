package com.example.set.data.model;

public class BaseResponse {
    public static final String STATUS_CONNECTION_FAILED = "connection_failed";
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_SUCCESS = "ok";
    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return status.equals(STATUS_SUCCESS);
    }
}
