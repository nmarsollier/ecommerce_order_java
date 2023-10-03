package com.order.utils.server;

import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Environment {

    @Inject
    public Environment() {

    }

    @SerializedName("serverPort")
    private Integer serverPort = 3004;
    @SerializedName("securityServerUrl")
    private String securityServerUrl = "http://localhost:3000";
    @SerializedName("rabbitServerUrl")
    private String rabbitServerUrl = "localhost";
    @SerializedName("databaseUrl")
    private String databaseUrl = "mongodb://localhost";
    @SerializedName("staticLocation")
    private String staticLocation = "www";


    {
        String port = System.getenv("SERVER_PORT");
        if (port != null && !port.isEmpty() && Integer.parseInt(port) != 0) {
            serverPort = Integer.parseInt(port);
        }
        String authService = System.getenv("AUTH_SERVICE_URL");
        if (authService != null && !authService.isEmpty()) {
            securityServerUrl = authService;
        }
        String rabbitUrl = System.getenv("RABBIT_URL");
        if (rabbitUrl != null && !rabbitUrl.isEmpty()) {
            rabbitServerUrl = rabbitUrl;
        }
        String mongoUrl = System.getenv("MONGO_URL");
        if (mongoUrl != null && !mongoUrl.isEmpty()) {
            databaseUrl = mongoUrl;
        }
        String wwwPath = System.getenv("WWW_PATH");
        if (wwwPath != null && !wwwPath.isEmpty()) {
            staticLocation = wwwPath;
        }
    }

    public Integer serverPort() {
        return serverPort;
    }

    public String securityServerUrl() {
        return securityServerUrl;
    }

    public String rabbitServerUrl() {
        return rabbitServerUrl;
    }

    public String databaseUrl() {
        return databaseUrl;
    }

    public String staticLocation() {
        return staticLocation;
    }

}