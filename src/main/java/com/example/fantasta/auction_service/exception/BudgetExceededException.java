package com.example.fantasta.auction_service.exception;

public class BudgetExceededException extends Exception
{
    public BudgetExceededException(String message) {
        super(message);
    }
    
}
