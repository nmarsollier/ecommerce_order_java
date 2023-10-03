package com.order.utils.errors;

public class UnauthorizedError extends SimpleError {
    public UnauthorizedError() {
        super(401, "Unauthorized");
    }
}
