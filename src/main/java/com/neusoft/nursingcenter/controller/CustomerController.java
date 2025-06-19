package com.neusoft.nursingcenter.controller;

import java.util.List;
import java.util.Map;

import com.neusoft.nursingcenter.service.CustomerService;
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

	@Autowired
	private CustomerService customerService;
	
	@RequestMapping("/pageAll")
	public PageResponseBean<List<Customer>> page(@RequestBody Map<String, Object> request){
		int current = (int) request.get("current"); //当前页面
		int size = (int) request.get("size"); //一页的行数

		IPage<Customer> page = new Page<>(current, size);
		QueryWrapper<Customer> qw = new QueryWrapper<>();
		qw.eq("is_deleted", 0); //要筛选没有被删除的
		IPage<Customer> result = customerMapper.selectPage(page, qw);

		List<Customer> list =result.getRecords();
		long total =result.getTotal();
		PageResponseBean<List<Customer>> prb = null;
		if(total > 0) {
			prb = new PageResponseBean<>(list);
			prb.setTotal(total);
		}else {
			prb = new PageResponseBean<>(500,"No data");
		}
		return prb;
	}

	// 用于入住登记页的多条件组合的分页查询
	// 组合条件：客户姓名、客户类型（自理老人 / 护理老人）
	@RequestMapping("/page")
	public PageResponseBean<List<Customer>> pageWithConditions(@RequestBody Map<String, Object> request){
		// int current, int size, String name, int customerType
		int current = (int) request.get("current"); //当前页面
		int size = (int) request.get("size"); //一页的行数
		String name = (String) request.get("name");
		int customerType = (int) request.get("customerType");
		
		IPage<Customer> page = new Page<>(current,size);
		QueryWrapper<Customer> qw = new QueryWrapper<>();
		qw.like("name", name);
		qw.eq("customer_type", customerType);
		qw.eq("is_deleted", 0); //要筛选没有被删除的
		IPage<Customer> result = customerMapper.selectPage(page,qw);

		List<Customer> list =result.getRecords();
		long total =result.getTotal();
		PageResponseBean<List<Customer>> prb =null;
		if(total > 0) {
			prb = new PageResponseBean<>(list);
			prb.setTotal(total);
		}else {
			prb = new PageResponseBean<>(500,"No data");
		}
		return prb;
	}

	// 事务
	@PostMapping("/add")
	public ResponseBean<Integer> add(@RequestBody Customer customer) {
		ResponseBean<Integer> rb = null;

		try {
			int result = customerService.addCustomer(customer);
			if(result > 0) {
				rb = new ResponseBean<>(result);
			}else {
				rb = new ResponseBean<>(500,"Fail to update");
			}
		} catch (Exception e) {
			rb = new ResponseBean<>(500, e.getMessage());
		}
		return rb;
	}

	// 事务
	// 注意前端修改客户信息时是不允许修改其所属的房间和床位的！床位这部分的修改只能在床位管理模块进行
	@PostMapping("/update")
	public ResponseBean<Integer> update(@RequestBody Customer customer) {
		ResponseBean<Integer> rb = null;

		try {
			int result = customerService.updateCustomer(customer);
			if(result > 0) {
				rb = new ResponseBean<>(result);
			}else {
				rb = new ResponseBean<>(500,"Fail to update");
			}
		} catch (Exception e) {
			rb = new ResponseBean<>(500, e.getMessage());
		}
		return rb;
	}

	// 事务
	@PostMapping("/deleteById")
	public ResponseBean<Integer> deleteById(@RequestBody Map<String, Object> request) {
		// 逻辑删除
		// 注意删除时需要将该用户正在使用的床位使用记录也删除；并将对应床位状态修改为空闲；该复杂操作需要用到Service
		int customerId = (int) request.get("customerId");

		ResponseBean<Integer> rb = null;
		try {
			int result = customerService.deleteCustomerById(customerId);
			if(result > 0) {
				rb = new ResponseBean<>(result);
			}else {
				rb = new ResponseBean<>(500,"Fail to delete");
			}
		} catch (Exception e) {
			rb = new ResponseBean<>(500, e.getMessage());
		}
		
		return rb;
	}


}
