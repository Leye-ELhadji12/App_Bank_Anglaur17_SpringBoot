package com.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data @Entity
@NoArgsConstructor @AllArgsConstructor
public class Operation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationdate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private TypeOp typeOp;
    @ManyToOne
    private Account account;
    private String description;
}
