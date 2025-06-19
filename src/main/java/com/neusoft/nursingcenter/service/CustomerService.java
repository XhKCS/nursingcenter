package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.Customer;

public interface CustomerService {
	int deleteCustomerById(int customerId);

	int updateCustomer(Customer updatedCustomer);

	int addCustomer(Customer customer);
}
