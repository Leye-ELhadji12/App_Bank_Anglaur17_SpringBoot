package com.backend.Execption;

public class AccountNotFound extends Exception {
    public AccountNotFound(String accountNotFound) {
        super(accountNotFound);
    }
}
