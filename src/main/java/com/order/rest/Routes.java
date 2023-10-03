package com.order.rest;


import com.google.gson.Gson;
import com.order.rest.tools.ErrorHandler;
import com.order.utils.gson.Builder;
import com.order.utils.server.Environment;
import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Type;

@Singleton
public class Routes {
    final Environment environment;
    final ErrorHandler errorHandler;
    final GetOrders getOrders;
    final GetOrdersBatchPaymentDefined getOrdersBatchPaymentDefined;
    final GetOrdersBatchPlaced getOrdersBatchPlaced;
    final GetOrdersBatchValidated getOrdersBatchValidated;
    final GetOrdersId getOrdersId;
    final PostOrdersIdPayment postOrdersIdPayment;
    final Gson gson = Builder.gson();

    @Inject
    public Routes(
            Environment environment,
            ErrorHandler errorHandler,
            GetOrders getOrders,
            GetOrdersBatchPaymentDefined getOrdersBatchPaymentDefined,
            GetOrdersBatchPlaced getOrdersBatchPlaced,
            GetOrdersBatchValidated getOrdersBatchValidated,
            GetOrdersId getOrdersId,
            PostOrdersIdPayment postOrdersIdPayment
    ) {
        this.environment = environment;
        this.errorHandler = errorHandler;
        this.getOrders = getOrders;
        this.getOrdersBatchPaymentDefined = getOrdersBatchPaymentDefined;
        this.getOrdersBatchPlaced = getOrdersBatchPlaced;
        this.getOrdersBatchValidated = getOrdersBatchValidated;
        this.getOrdersId = getOrdersId;
        this.postOrdersIdPayment = postOrdersIdPayment;
    }


    public void init() {
        JsonMapper gsonMapper = new JsonMapper() {
            @Override
            public @NotNull String toJsonString(@NotNull Object obj, @NotNull Type type) {
                return gson.toJson(obj, type);
            }

            @Override
            public <T> @NotNull T fromJsonString(@NotNull String json, @NotNull Type targetType) {
                return gson.fromJson(json, targetType);
            }
        };

        Javalin app = Javalin.create(config -> {
                    config.jsonMapper(gsonMapper);
                    config.plugins.enableCors(cors -> cors.add(CorsPluginConfig::anyHost));
                    config.staticFiles.add(environment.staticLocation());
                }
        );
        app.start(environment.serverPort());

        errorHandler.init(app);
        getOrders.init(app);
        getOrdersBatchPaymentDefined.init(app);
        getOrdersBatchPlaced.init(app);
        getOrdersBatchValidated.init(app);
        getOrdersId.init(app);
        postOrdersIdPayment.init(app);
    }
}
