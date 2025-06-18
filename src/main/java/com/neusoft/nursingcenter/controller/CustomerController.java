package com.neusoft.nursingcenter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.Customer;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.CustomerMapper;

@CrossOrigin("*")
@RestController
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CustomerMapper customerMapper;
	
	@GetMapping("/getPagedCustomers")
	public PageResponseBean<List<Customer>> getPagedCustomers(@RequestBody Map<String, Object> request){
		Long cur = (Long) request.get("cur");
		Long number = (Long) request.get("number");
		IPage<Customer> page = new Page<>(cur,number);
		IPage<Customer> result = customerMapper.selectPage(page,null);
		List<Customer> list =result.getRecords();
		Long total =result.getTotal();
		
		PageResponseBean<List<Customer>> prb =null;
		if(total > 0) {
			prb = new PageResponseBean<>(list);
			prb.setTotal(total);
		}else {
			prb = new PageResponseBean<>(500,"No data");
		}
		return prb;
	}
	
	@GetMapping("/getPagedCustomersByName")
	public PageResponseBean<List<Customer>> getPagedCustomersByName(@RequestBody Map<String, Object> request){
		// Long cur,Long number,String name
		Long cur = (Long) request.get("cur");
		Long number = (Long) request.get("number");
		String name = (String) request.get("name");
		
		IPage<Customer> page = new Page<>(cur,number);
		QueryWrapper<Customer> qw = new QueryWrapper<>();
		qw.like("name", name);
		IPage<Customer> result = customerMapper.selectPage(page,qw);
		List<Customer> list =result.getRecords();
		Long total =result.getTotal();
		
		PageResponseBean<List<Customer>> prb =null;
		if(total > 0) {
			prb = new PageResponseBean<>(list);
			prb.setTotal(total);
		}else {
			prb = new PageResponseBean<>(500,"No data");
		}
		return prb;
	}
	
	@PostMapping("/addCustomer")
	public ResponseBean<Integer> addCustomer(@RequestBody Customer customer) {
		Integer result = customerMapper.insert(customer);
		ResponseBean<Integer> rb = null;
		if(result > 0) {
			rb = new ResponseBean<>(result);
		}else {
			rb = new ResponseBean<>(500,"Fail to add");
		}
		return rb;
	}
	
	@PostMapping("/updateCustomer")
	public ResponseBean<Integer> updateCustomer(@RequestBody Customer data) {
		Integer result = customerMapper.updateById(data);
		ResponseBean<Integer> rb = null;
		if(result > 0) {
			rb = new ResponseBean<>(result);
		}else {
			rb = new ResponseBean<>(500,"Fail to update");
		}
		return rb;
	}
	
	@PostMapping("/deleteCustomer")
	public ResponseBean<Integer> deleteCustomer(@RequestBody Map<String, Object> request) {
		int id = (int) request.get("id");
		Customer customer = customerMapper.selectById(id);
		customer.setDeleted(true);
		Integer result = customerMapper.updateById(customer);
		ResponseBean<Integer> rb =null;
		if(result > 0) {
			rb = new ResponseBean<>(result);
		}else {
			rb = new ResponseBean<>(500,"Fail to delete");
		}
		
		return rb;
	}


}
