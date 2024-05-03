package com.backend.dto;

import com.backend.entity.Status;
import lombok.Data;

import java.util.Date;

@Data
public class SavingAccountDTO extends AccountDTO{
    private String id;
    private double balance;
    private Date createdate;
    private Status status;
    private CustomerDTO customerDTO;
    private double interestRate;
}
