package com.backend.controller;

import com.backend.Execption.AccountNotFound;
import com.backend.dto.AccountDTO;
import com.backend.dto.AccountHistoryDTO;
import com.backend.dto.OperationDTO;
import com.backend.service.AccountServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.LongStream;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountServiceImpl accountService;

    @GetMapping("/accounts/{accountId}")
    public AccountDTO getAccount(String accountId) throws AccountNotFound {
        return accountService.getAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<AccountDTO> AccountsList() {
        return accountService.accountList();
    }

    @GetMapping("/accounts/{accountId}/operationsHistories")
    public AccountHistoryDTO getHistoriesAccount(@PathVariable String accountId,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "5") int size) {
        return accountService.gethistoriesAccountOperation(accountId, page, size);
    }
}
