package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.model.CustomerDTO;
import com.example.spring6restmvc.services.CustomerService;
import com.example.spring6restmvc.services.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void patchCustomer() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.listCustomers().get(0);

        HashMap<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New name");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomer(eq(testCustomer.getId()), customerArgumentCaptor.capture());

        assertThat(customerArgumentCaptor.getValue().getCustomerName()).isEqualTo(customerMap.get("customerName"));
    }

    @Test
    void deleteCustomer() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.listCustomers().get(0);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId()))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(eq(testCustomer.getId()));
    }

    @Test
    void handlePut() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.listCustomers().get(0);

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomer(eq(testCustomer.getId()), any(CustomerDTO.class));
    }

    @Test
    void saveCustomer() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.listCustomers().get(0);
        testCustomer.setId(null);
        testCustomer.setVersion(null);

        given(customerService.saveCustomer(any(CustomerDTO.class))).willReturn(customerServiceImpl.listCustomers().get(1));

        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void listCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void getCustomerByIdNotFound() throws Exception {

        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerById() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.listCustomers().get(0);

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(testCustomer.getCustomerName())));

    }
}