package com.backend.dto;

import com.backend.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class CurrentAccountDTO extends AccountDTO{
    private String id;
    private double balance;
    private Date createdate;
    private Status status;
    private CustomerDTO customerDTO;
    private double overdraft;
}
