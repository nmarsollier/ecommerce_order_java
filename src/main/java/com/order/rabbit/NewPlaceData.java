package com.order.rabbit;

import com.google.gson.annotations.SerializedName;

import com.order.utils.gson.Builder;
import com.order.utils.validator.MinLen;
import com.order.utils.validator.MinValue;
import com.order.utils.validator.NotEmpty;
import com.order.utils.validator.Required;
import com.order.utils.validator.Validate;

public class NewPlaceData {
    @SerializedName("cartId")
    @Required
    @MinLen(1)
    public String cartId;

    @SerializedName("userId")
    @Required
    @MinLen(1)
    public String userId;

    @SerializedName("articles")
    @Required
    @NotEmpty
    @Validate
    public Article[] articles;

    public static class Article {
        @SerializedName("id")
        @Required
        @MinLen(1)
        public String id;

        @SerializedName("quantity")
        @Required
        @MinValue(1)
        public int quantity;
    }

    public static NewPlaceData fromJson(String json) {
        return Builder.gson().fromJson(json, NewPlaceData.class);
    }
}
