package com.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private List<OperationDTO> operationDTOList;
    private String accountId;
    private double balance;
    private int totalPages;
    private int currentPage;
    private int sizePage;
}
