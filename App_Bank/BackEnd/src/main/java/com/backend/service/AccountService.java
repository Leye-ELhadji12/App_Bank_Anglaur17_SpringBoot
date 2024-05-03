package com.backend.service;

import com.backend.Execption.AccountNotFound;
import com.backend.Execption.BalanceNotEnoughException;
import com.backend.Execption.CustomerNotFound;
import com.backend.dto.*;
import com.backend.entity.Account;
import com.backend.entity.CurrentAccount;
import com.backend.entity.SavingAccount;

import java.util.List;

public interface AccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentAccountDTO saveCurrentAccount(double initBalance, double overDraft, Long customerId) throws CustomerNotFound;
    SavingAccountDTO saveSavingAccount(double initBalance, double interest, Long customerId) throws CustomerNotFound;

    List<CustomerDTO> customerList();
    AccountDTO getAccount(String accountId) throws AccountNotFound;
    void debit(String accountId, double amount, String description) throws AccountNotFound, BalanceNotEnoughException;
    void credit(String accountId, double amount, String description) throws AccountNotFound;
    void transfer(String accountSource, String accountDestination, double amount) throws BalanceNotEnoughException, AccountNotFound;
    List<AccountDTO> accountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFound;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<OperationDTO> historiesAccountOperation(String accoundId);

    AccountHistoryDTO gethistoriesAccountOperation(String accountId, int page, int size);

    List<CustomerDTO> searchCustomer(String word);
}
