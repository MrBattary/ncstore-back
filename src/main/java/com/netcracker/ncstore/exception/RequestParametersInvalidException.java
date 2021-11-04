package com.netcracker.ncstore.exception;

/**
 * This exception is used when HTTP request parameters are incorrect
 * and server can not process the request.
 */
public class RequestParametersInvalidException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Request parameters are invalid. Check API for making a correct request.";
    }
}
