package com.order.rabbit.dto;

import com.google.gson.annotations.SerializedName;
import com.order.events.schema.PlaceEvent;
import com.order.utils.gson.Builder;
import com.order.utils.validator.*;

import java.util.Arrays;

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

    public PlaceEvent toPlaceEvent() {
        PlaceEvent.Article[] articles = Arrays.stream(this.articles) //
                .map(a -> new PlaceEvent.Article(a.id, a.quantity)) //
                .toList() //
                .toArray(new PlaceEvent.Article[]{});

        return new PlaceEvent(this.cartId, this.userId, articles);
    }
}
