package com.order.rest;

import com.order.security.TokenService;
import com.order.security.User;
import com.order.utils.errors.SimpleError;
import com.order.utils.errors.UnauthorizedError;
import com.order.utils.errors.ValidationError;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Validations {
    @Autowired
    TokenService tokenService;

    public void validateUser(String authHeader) throws SimpleError {
        if (authHeader == null) throw new UnauthorizedError();
        tokenService.validateAdmin(authHeader);
    }

    public User currentUser(String authHeader) throws SimpleError {
        return tokenService.getUser(authHeader);
    }

    public void validateOrderId(String orderId) throws ValidationError {
        try {
            if (orderId.isBlank()) {
                throw new ValidationError().addPath("id", "Not found");
            }

            new ObjectId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationError().addPath("id", "Not found");
        }
    }
}

