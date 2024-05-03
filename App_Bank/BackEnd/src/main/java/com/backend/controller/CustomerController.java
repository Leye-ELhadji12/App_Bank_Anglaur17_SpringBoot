package com.backend.controller;

import com.backend.Execption.CustomerNotFound;
import com.backend.dto.CustomerDTO;
import com.backend.entity.Customer;
import com.backend.service.AccountServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
//@RequestMapping("/customer")
public class CustomerController {

    private final AccountServiceImpl accountServiceImpl;

    @GetMapping("/customers")
    public List<CustomerDTO> customerList() {
        return accountServiceImpl.customerList();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFound {
        return accountServiceImpl.getCustomer(customerId);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return accountServiceImpl.saveCustomer(customerDTO);
    }

    @GetMapping("/customers/search")
    public List<CustomerDTO> seachCustomer(@RequestParam(name = "word", defaultValue = "") String word) {
        return accountServiceImpl.searchCustomer("%"+word+"%");
    }

    @PutMapping("/customer/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(customerId);
        return accountServiceImpl.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customer/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        accountServiceImpl.deleteCustomer(id);
    }
}
