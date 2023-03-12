package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();

    Optional<Customer> getCustomerById(UUID id);

    Customer saveCustomer(Customer customer);

    void updateCustomer(UUID customerId, Customer customer);

    void deleteCustomer(UUID customerId);

    void patchCustomer(UUID customerId, Customer customer);
}
