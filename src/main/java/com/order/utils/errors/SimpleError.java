package com.order.utils.errors;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.order.utils.gson.GsonError;
import com.order.utils.gson.SkipSerialization;

/**
 * Es un error simple que se puede serializar como Json.
 */
public class SimpleError extends Exception implements GsonError {
    private static final long serialVersionUID = 1L;

    @SkipSerialization
    int statusCode = 500;

    @SerializedName("error")
    final
    String error;

    public SimpleError(int statusCode, String error) {
        this.statusCode = statusCode;
        this.error = error;
    }

    public SimpleError(String error) {
        this.error = error;
    }

    public String toJson() {
        SerializedMessage msg = new SerializedMessage();
        msg.error = error;
        return new Gson().toJson(msg);
    }

    class SerializedMessage {
        @SerializedName("error")
        String error;
    }

}