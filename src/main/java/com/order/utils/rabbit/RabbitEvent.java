package com.order.utils.rabbit;

import com.google.gson.annotations.SerializedName;

import com.order.utils.gson.Builder;
import com.order.utils.gson.GsonSerializable;
import com.order.utils.validator.Required;

public class RabbitEvent implements GsonSerializable {
    // tipo de mensaje enviado
    @SerializedName("type")
    @Required
    public String type;

    // Version del protocolo
    @SerializedName("version")
    public int version;

    // Por si el destinatario necesita saber de donde viene el mensaje
    @SerializedName("queue")
    public String queue;

   // Por si el destinatario necesita saber de donde viene el mensaje
    @SerializedName("exchange")
    public String exchange;

    // El body del mensaje
    @SerializedName("message")
    @Required
    public Object message;

    public static RabbitEvent fromJson(String json) {
        return Builder.gson().fromJson(json, RabbitEvent.class);
    }

    @Override
    public String toJson() {
        return Builder.gson().toJson(this);
    }
}
