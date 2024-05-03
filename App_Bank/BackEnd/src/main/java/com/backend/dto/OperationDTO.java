package com.backend.dto;

import com.backend.entity.Account;
import com.backend.entity.TypeOp;
import lombok.Data;

import java.util.Date;

@Data
public class OperationDTO {
    private Long id;
    private Date operationdate;
    private double amount;
    private TypeOp typeOp;
    private String description;
}
