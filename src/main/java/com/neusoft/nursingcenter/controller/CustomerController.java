package com.neusoft.nursingcenter.controller;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.nursingcenter.entity.NursingLevel;
import com.neusoft.nursingcenter.mapper.NursingLevelMapper;
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

	@Autowired
	private NursingLevelMapper nursingLevelMapper;

	@PostMapping("/listAll")
	public ResponseBean<List<Customer>> listAll() {
		QueryWrapper<Customer> qw = new QueryWrapper<>();
		qw.eq("is_deleted", 0); //要筛选没有被删除的

		List<Customer> customerList = customerMapper.selectList(qw);
		ResponseBean<List<Customer>> rb = null;

		if (customerList.size() > 0) {
			rb = new ResponseBean<>(customerList);
		} else {
			rb = new ResponseBean<>(500, "No data");
		}
		return rb;
	}

	// 组合条件：客户姓名、客户类型（自理老人 / 护理老人）
	@PostMapping("/list")
	public ResponseBean<List<Customer>> list(@RequestBody Map<String, Object> request) {
		String name = (String) request.get("name");

		QueryWrapper<Customer> qw = new QueryWrapper<>();
		qw.like("name", name);
		if(request.get("customerType")!=null){
			int customerType = (int) request.get("customerType");
			qw.eq("customer_type",customerType);
		}
		qw.eq("is_deleted", 0); //要筛选没有被删除的
		List<Customer> customerList = customerMapper.selectList(qw);
		ResponseBean<List<Customer>> rb = null;

		if (customerList.size() > 0) {
			rb = new ResponseBean<>(customerList);
		} else {
			rb = new ResponseBean<>(500, "No data");
		}
		return rb;
	}

	@PostMapping("/pageAll")
	public PageResponseBean<List<Customer>> pageAll(@RequestBody Map<String, Object> request){
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
	@PostMapping("/page")
	public PageResponseBean<List<Customer>> page(@RequestBody Map<String, Object> request){
		// int current, int size, String name, int customerType
		int current = (int) request.get("current"); //当前页面
		int size = (int) request.get("size"); //一页的行数
		String name = (String) request.get("name");

		IPage<Customer> page = new Page<>(current,size);

		QueryWrapper<Customer> qw = new QueryWrapper<>();
		qw.like("name", name);
		qw.eq("is_deleted", 0); //要筛选没有被删除的

		if(request.get("customerType")!=null){
			int customerType = (int) request.get("customerType");
			qw.eq("customer_type",customerType);
		}

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
	public ResponseBean<Integer> add(@RequestBody Map<String, Object> request) {
		ResponseBean<Integer> rb = null;
		int customerType = (int) request.get("customerType");
		String name = (String) request.get("name");
		String idCard = (String) request.get("idCard");
		int age = (int) request.get("age");
		int gender = (int) request.get("gender");
		String bloodType = (String) request.get("bloodType");
		String relative = (String) request.get("relative");
		String phoneNumber = (String) request.get("phoneNumber");
//		String building = (String) request.get("building");
		String roomNumber = (String) request.get("roomNumber");
		String bedNumber = (String) request.get("bedNumber");
		String checkinDate = (String) request.get("checkinDate");
		String expirationDate = (String) request.get("expirationDate");

		Customer customer = new Customer(0, customerType, 0, "", name, idCard, age, gender, bloodType, relative, phoneNumber, "606", roomNumber, bedNumber, checkinDate, expirationDate, false);
		try {
			int result = customerService.addCustomer(customer);
			if(result > 0) {
				rb = new ResponseBean<>(result);
			}else {
				rb = new ResponseBean<>(500,"Fail to update");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
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

	@PostMapping("/setNursingLevel")
	public ResponseBean<Integer> setNursingLevel(@RequestBody Map<String, Object> request) {
		ResponseBean<Integer> rb = null;
		int customerId = (int) request.get("customerId");
		int levelId = (int) request.get("levelId");
		Customer customer = customerMapper.selectById(customerId);
		NursingLevel nursingLevel = nursingLevelMapper.selectById(levelId);
		if (customer == null || nursingLevel == null) {
			return new ResponseBean<>(500, "客户或护理级别不存在");
		}

		customer.setNursingLevelName(nursingLevel.getName());
		int result = customerMapper.updateById(customer);
		if(result > 0) {
			rb = new ResponseBean<>(result);
		}else {
			rb = new ResponseBean<>(500,"护理级别设置失败");
		}
		return rb;
	}

	// 事务
	@PostMapping("/delete")
	public ResponseBean<Integer> delete(@RequestBody Map<String, Object> request) {
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
			System.out.println(e.getMessage());
			rb = new ResponseBean<>(500, e.getMessage());
		}
		
		return rb;
	}


}
