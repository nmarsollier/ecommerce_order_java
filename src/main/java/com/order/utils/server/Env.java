package com.order.utils.server;

import com.google.gson.annotations.SerializedName;
import org.springframework.stereotype.Service;

@Service
public class Env {

    @SerializedName("securityServerUrl")
    private String securityServerUrl = "http://localhost:3000";
    @SerializedName("rabbitServerUrl")
    private String rabbitServerUrl = "localhost";
    @SerializedName("staticLocation")
    private String staticLocation = "www";


    {
        String port = System.getenv("SERVER_PORT");
        String authService = System.getenv("AUTH_SERVICE_URL");
        if (authService != null && !authService.isEmpty()) {
            securityServerUrl = authService;
        }
        String rabbitUrl = System.getenv("RABBIT_URL");
        if (rabbitUrl != null && !rabbitUrl.isEmpty()) {
            rabbitServerUrl = rabbitUrl;
        }
        String wwwPath = System.getenv("WWW_PATH");
        if (wwwPath != null && !wwwPath.isEmpty()) {
            staticLocation = wwwPath;
        }
    }


    public String securityServerUrl() {
        return securityServerUrl;
    }

    public String rabbitServerUrl() {
        return rabbitServerUrl;
    }

    public String staticLocation() {
        return staticLocation;
    }

}