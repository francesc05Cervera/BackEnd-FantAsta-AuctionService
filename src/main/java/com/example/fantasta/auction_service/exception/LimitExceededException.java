package com.example.fantasta.auction_service.exception;

public class LimitExceededException extends Exception
{
    public LimitExceededException(String message) {
        super(message);
    }
    
}
