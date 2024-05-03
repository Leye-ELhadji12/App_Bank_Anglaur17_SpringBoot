package com.backend;

import com.backend.Execption.AccountNotFound;
import com.backend.Execption.BalanceNotEnoughException;
import com.backend.Execption.CustomerNotFound;
import com.backend.dto.AccountDTO;
import com.backend.dto.CurrentAccountDTO;
import com.backend.dto.SavingAccountDTO;
import com.backend.dto.CustomerDTO;
import com.backend.entity.*;
import com.backend.repository.AccountRepository;
import com.backend.repository.CustomerRepository;
import com.backend.repository.OperationRepository;
import com.backend.service.AccountServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(AccountServiceImpl accountServiceImpl){
		return args -> {
			Stream.of("Doudou", "Omar", "Oumy").forEach(name ->
			{
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
	 			accountServiceImpl.saveCustomer(customer);
			});
			accountServiceImpl.customerList().forEach(customer ->
			{
                try {
                    accountServiceImpl.saveCurrentAccount(Math.random()*10000, 10000, customer.getId());
                	accountServiceImpl.saveSavingAccount(Math.random()*10000, 5.5, customer.getId());

				} catch (CustomerNotFound e) {
                    throw new RuntimeException("Saving not possible");
                }
            });
			List<AccountDTO> accounts = accountServiceImpl.accountList();
			for (AccountDTO account : accounts) {
				for (int i = 0; i < 10; i++) {
					String accountId;
					if (account instanceof SavingAccountDTO) {
						accountId = ((SavingAccountDTO) account).getId();
					}
					else{
						accountId = ((CurrentAccountDTO) account).getId();
					}
					accountServiceImpl.credit(accountId, Math.random() * 10000 + 10000, "Credit");
                    try {
                        accountServiceImpl.debit(accountId, 1000 + Math.random() * 10000, "Debit");
                    } catch (BalanceNotEnoughException e) {
                        throw new RuntimeException(e);
                    }
                }
			}
		};
	}
	//@Bean
	CommandLineRunner begin(CustomerRepository customerRepository,
						  AccountRepository accountRepository,
						  OperationRepository operationRepository){
		return args -> {
			Stream.of("Doudou","Omar", "Oumy").forEach(name ->
			{
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(customer ->
			{
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*100000);
				currentAccount.setCreatedate(new Date());
				//currentAccount.setCustomer(customer);
				currentAccount.setOverdraft(10000);
				currentAccount.setStatus(Status.CREATED);
				accountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*100000);
				savingAccount.setCreatedate(new Date());
				//savingAccount.setCustomer(customer);
				savingAccount.setInterestRate(5.5);
				savingAccount.setStatus(Status.CREATED);
				accountRepository.save(savingAccount);
			});
			accountRepository.findAll().forEach(acc ->
			{
				for (int i = 0; i < 10; i++) {
					Operation operation = new Operation();
					operation.setOperationdate(new Date());
					operation.setAmount(Math.random()*10000);
					operation.setTypeOp(Math.random()>0.5 ? TypeOp.DEBIT : TypeOp.CREDIT);
					operation.setAccount(acc);
					operationRepository.save(operation);
				}
			});
		};
	}
}
