package com.backend.repository;

import com.backend.dto.CustomerDTO;
import com.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM  Customer c WHERE c.name like :wd")
    List<Customer> searchCustomerByName(@Param("wd") String word);
}
