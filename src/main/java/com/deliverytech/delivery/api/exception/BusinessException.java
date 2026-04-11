package com.deliverytech.delivery.api.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message){
        super(message);
    }
}