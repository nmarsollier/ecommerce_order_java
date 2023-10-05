
package com.order.rabbit.dto;

import com.google.gson.annotations.SerializedName;
import com.order.events.schema.ArticleValidationEvent;
import com.order.utils.gson.Builder;
import com.order.utils.validator.MinLen;
import com.order.utils.validator.Required;

public class NewArticleValidationData {
    @SerializedName("referenceId")
    @Required
    @MinLen(1)
    public String orderId;

    @SerializedName("articleId")
    @Required
    @MinLen(1)
    public String articleId;

    @SerializedName("valid")
    public boolean valid;

    @SerializedName("stock")
    public int stock;

    @SerializedName("price")
    public double price;

    public static NewArticleValidationData fromJson(String json) {
        return Builder.gson().fromJson(json, NewArticleValidationData.class);
    }

    public ArticleValidationEvent toArticleValidationEvent() {
        return new ArticleValidationEvent(this.articleId, this.valid, this.stock, this.price);
    }
}
