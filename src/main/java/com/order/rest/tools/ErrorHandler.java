package com.order.rest.tools;

import com.order.utils.errors.SimpleError;
import com.order.utils.errors.UnauthorizedError;
import com.order.utils.errors.ValidationError;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;


/**
 * Es un Helper para serializar correctamente los errores del sistema.
 *
 * @apiDefine Errors
 * @apiErrorExample 400 Bad Request
 * HTTP/1.1 400 Bad Request
 * {
 * "path" : "{Nombre de la propiedad}",
 * "message" : "{Motivo del error}"
 * }
 * @apiErrorExample 400 Bad Request
 * HTTP/1.1 400 Bad Request
 * {
 * "error" : "{Motivo del error}"
 * }
 * @apiErrorExample 500 Server Error
 * HTTP/1.1 500 Server Error
 * {
 * "error" : "{Motivo del error}"
 * }
 */
@Singleton
public class ErrorHandler {
    private static final HashMap<String, String> INTERNAL_ERROR = new HashMap<>();

    @Inject
    public ErrorHandler() {

    }

    static {
        INTERNAL_ERROR.put("error", "Internal Server Error");
    }

    public void init(Javalin app) {
        app.exception(ValidationError.class, (ex, ctx) -> {
            ctx.status(400).json(ex.toJson());
        });

        app.exception(SimpleError.class, (ex, ctx) -> {
            ctx.status(400).json(ex.toJson());
        });

        app.exception(UnauthorizedError.class, (ex, ctx) -> {
            ctx.status(401).json(ex.toJson());
        });

        app.exception(Exception.class, (ex, ctx) -> {
            ctx.status(500).json(INTERNAL_ERROR);
        });
    }
}