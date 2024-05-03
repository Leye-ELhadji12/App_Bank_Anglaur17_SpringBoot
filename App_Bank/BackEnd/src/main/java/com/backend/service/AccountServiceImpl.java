package com.backend.service;

import com.backend.Execption.AccountNotFound;
import com.backend.Execption.BalanceNotEnoughException;
import com.backend.Execption.CustomerNotFound;
import com.backend.dto.*;
import com.backend.entity.Account;
import com.backend.entity.*;
import com.backend.mapper.AccountMapper;
import com.backend.repository.AccountRepository;
import com.backend.repository.CustomerRepository;
import com.backend.repository.OperationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j @AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final AccountMapper accountMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("new Customer");
        Customer customer = accountMapper.customer(customerDTO);
        Customer save_customer = customerRepository.save(customer);
        return accountMapper.customerDTO(save_customer);
    }

    @Override
    public CurrentAccountDTO saveCurrentAccount(double initBalance, double overDraft, Long customerId) throws CustomerNotFound {
        log.info("Save Account");
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->new CustomerNotFound("This customer doesn't exit"));
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedate(new Date());
        currentAccount.setBalance(initBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverdraft(overDraft);

        return accountMapper.currentAccount(accountRepository.save(currentAccount));
    }

    @Override
    public SavingAccountDTO saveSavingAccount(double initBalance, double interest, Long customerId) throws CustomerNotFound {
        log.info("Save Account");
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->new CustomerNotFound("This customer doesn't exit"));
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedate(new Date());
        savingAccount.setBalance(initBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interest);

        return accountMapper.savingAccount(accountRepository.save(savingAccount));
    }

    @Override
    public List<CustomerDTO> customerList() {
        List<Customer> customers = customerRepository.findAll();
        //        List<CustomerDTO> customerDTOS = new ArrayList<>();
//        for (Customer customer: customers){
//            CustomerDTO customerDTO = accountMapper.customerDTO(customer);
//            customerDTOS.add(customerDTO);
//        }
        return customers.stream().map(accountMapper::customerDTO).toList();
    }

    @Override
    public AccountDTO getAccount(String accountId) throws AccountNotFound {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFound("Customer not found"));
        if (account instanceof SavingAccount savingAccount) {
            return accountMapper.savingAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) account;
            return accountMapper.currentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws AccountNotFound, BalanceNotEnoughException {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFound("Account not found"));
        if (account.getBalance() < amount)
            throw new BalanceNotEnoughException("Balance not enough");
        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setTypeOp(TypeOp.DEBIT);
        operation.setDescription(description);
        operation.setOperationdate(new Date());
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()-amount);
        accountRepository.save(account);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws AccountNotFound {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFound("Account not found"));
        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setTypeOp(TypeOp.CREDIT);
        operation.setDescription(description);
        operation.setOperationdate(new Date());
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()+amount);
        accountRepository.save(account);
    }

    @Override
    public void transfer(String accountSource, String accountDestination, double amount) throws BalanceNotEnoughException, AccountNotFound {
        debit(accountSource, amount, "Transfer to"+ accountDestination);
        credit(accountDestination, amount, "Tranfer to"+accountSource);
    }

    @Override
    public List<AccountDTO> accountList() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(account ->
        {
          if (account instanceof SavingAccount) {
              SavingAccount savingAccount = (SavingAccount) account;
              return accountMapper.savingAccount(savingAccount);
          } else  {
              CurrentAccount currentAccount= (CurrentAccount) account;
              return accountMapper.currentAccount(currentAccount);

          }
        }).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFound {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFound("Customer not found"));
        return accountMapper.customerDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("new Customer");
        Customer customer = accountMapper.customer(customerDTO);
        Customer save_customer = customerRepository.save(customer);
        return accountMapper.customerDTO(save_customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<OperationDTO> historiesAccountOperation(String accoundId) {
        List<Operation> operationList = operationRepository.findByAccountId(accoundId);
        return operationList.stream().map(accountMapper::accountOperation).collect(Collectors.toList());
    }
    @Override
    public AccountHistoryDTO gethistoriesAccountOperation(String accountId, int page, int size){
        Account account = accountRepository.findById(accountId).orElseThrow();
        Page<Operation> operations = operationRepository.findByAccountId(accountId, PageRequest.of(page, size));
        List<OperationDTO> operationDTOList = operations.getContent().stream().map(accountMapper::accountOperation).collect(Collectors.toList());
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setOperationDTOList(operationDTOList);
        accountHistoryDTO.setAccountId(account.getId());
        accountHistoryDTO.setBalance(account.getBalance());
        accountHistoryDTO.setSizePage(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(operations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomer(String word) {
        List<Customer> customerList = customerRepository.searchCustomerByName(word);
        return customerList.stream().map(accountMapper::customerDTO).collect(Collectors.toList());
    }
}
