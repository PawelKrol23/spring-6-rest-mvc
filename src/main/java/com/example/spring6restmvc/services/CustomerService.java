package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> listCustomers();

    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO saveCustomer(CustomerDTO customer);

    void updateCustomer(UUID customerId, CustomerDTO customer);

    void deleteCustomer(UUID customerId);

    void patchCustomer(UUID customerId, CustomerDTO customer);
}
