package com.backend.Execption;

public class BalanceNotEnoughException extends Throwable {
    public BalanceNotEnoughException(String balanceNotEnough) {
        super(balanceNotEnough);
    }
}
