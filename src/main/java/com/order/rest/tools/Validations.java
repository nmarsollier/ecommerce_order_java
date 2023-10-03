package com.order.rest.tools;

import com.order.security.TokenService;
import com.order.security.User;
import com.order.utils.errors.SimpleError;
import com.order.utils.errors.UnauthorizedError;
import com.order.utils.errors.ValidationError;
import io.javalin.http.Context;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Validations {
    final TokenService tokenService;

    @Inject
    public Validations(
            TokenService tokenService
    ) {
        this.tokenService = tokenService;
    }

    public void validateUser(Context ctx) throws SimpleError {
        String authHeader = ctx.header("Authorization");
        if (authHeader == null) throw new UnauthorizedError();
        tokenService.validateAdmin(authHeader);
    }

    public User currentUser(Context ctx) throws SimpleError {
        return tokenService.getUser(ctx.header("Authorization"));
    }

    public void validateOrderId(Context ctx) throws ValidationError {
        try {
            String id = ctx.pathParam("orderId");

            if (id.isBlank()) {
                throw new ValidationError().addPath("id", "Not found");
            }

            new ObjectId(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationError().addPath("id", "Not found");
        }
    }
}

