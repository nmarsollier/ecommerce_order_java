package com.order.utils.errors;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.order.utils.gson.GsonError;
import com.order.utils.gson.SkipSerialization;

/**
 * Un error de validaciones de atributos de una clase.
 * Estos errores se pueden serializar como Json.
 */
public class ValidationError extends Exception implements GsonError {
    private static final long serialVersionUID = 1L;

    @SkipSerialization
    public int statusCode = 400;

    @SerializedName("messages")
    final
    ArrayList<ValidationMessage> messages = new ArrayList<>();

    public ValidationError() {

    }

    public ValidationError(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public ValidationError addPath(String path, String message) {
        messages.add(new ValidationMessage(path, message));
        return this;
    }

    public String toJson() {
        SerializedMessage msg = new SerializedMessage();
        msg.messages = messages;
        return new Gson().toJson(msg);
    }

    class ValidationMessage {
        @SerializedName("path")
        final
        String path;
        @SerializedName("message")
        final
        String message;

        ValidationMessage(String path, String message) {
            this.path = path;
            this.message = message;
        }
    }

    class SerializedMessage {
        @SerializedName("messages")
        ArrayList<ValidationMessage> messages = new ArrayList<>();
    }

    @Override
    public void printStackTrace() {
        Logger.getLogger("ValidationError").log(Level.SEVERE, "Validation error : " + this.toJson());
    }
}