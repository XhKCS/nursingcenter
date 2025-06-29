package com.neusoft.nursingcenter.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.NursingLevelMapper;
import com.neusoft.nursingcenter.mapper.UserMapper;
import com.neusoft.nursingcenter.redisdao.RedisDao;
import com.neusoft.nursingcenter.service.CustomerService;
import com.neusoft.nursingcenter.util.JWTTool;
import jakarta.servlet.http.HttpServletRequest;
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

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RedisDao redisDao;

	@PostMapping("/login")
	public ResponseBean<String> login(@RequestBody Map<String, Object> request, HttpServletRequest httpServletRequest) {
		String phoneNumber = (String) request.get("phoneNumber");
		String password = (String) request.get("password");
		Customer dbCustomer = customerMapper.getByPhoneNumber(phoneNumber);
		ResponseBean<String> rb = null;

		if (dbCustomer == null) {
			rb = new ResponseBean<>(500, "该手机号的客户不存在");
			return rb;
		}
		if (dbCustomer.getPassword().equals(password)) {
			try {
				dbCustomer.setPassword(null);
				// 把登录客户对象转换为json
				ObjectMapper om=new ObjectMapper();
				String customerJson= om.writeValueAsString(dbCustomer);
				System.out.println("customerJson：" + customerJson);
				String token = JWTTool.createToken(customerJson);
				System.out.println("生成相应token：" + token);
				//	把令牌存入redis中一份，键为customer-{customerId}
				redisDao.set("customer-"+dbCustomer.getCustomerId().toString(), token, 600, TimeUnit.SECONDS);
				//	同时传递给前端一份
				rb = new ResponseBean<>(token);
			} catch (Exception e) {
				System.out.println("Exception happened: " + e.getMessage());
				rb = new ResponseBean<>(500, e.getMessage());
			}
		} else {
			rb = new ResponseBean<>(500, "登录密码错误");
		}
		return rb;
	}

	// 根据token加载当前已登录的customer对象
	@PostMapping("/load")
	public ResponseBean<Customer> load(HttpServletRequest httpServletRequest) {
		ResponseBean<Customer> rb = null;
		try {
			if (httpServletRequest.getHeader("token") != null) {
				String token = httpServletRequest.getHeader("token");
				// 获取客户json
				String customerJson = JWTTool.parseToken(token);
				// json转化为对象
				ObjectMapper om = new ObjectMapper();
				Customer customer = om.readValue(customerJson, Customer.class);
				System.out.println("从token中转换得到的customer："+customer);
				rb = new ResponseBean<>(customer);
			}
			else {
				rb = new ResponseBean<>(500, "登录已过期");
			}
		} catch (Exception e) {
			System.out.println("Exception happened: " + e.getMessage());
			rb = new ResponseBean<>(500, e.getMessage());
		}
		return rb;
	}

	@PostMapping("/logout")
	public ResponseBean<String> logout(HttpServletRequest httpServletRequest) {
		ResponseBean<String> rb = null;
		//从redis中删除存储的token
		try {
			if (httpServletRequest.getHeader("token") != null) {
				String token = httpServletRequest.getHeader("token");
				// 获取对象json
				String json = JWTTool.parseToken(token);
				// json转化为对象（User或Customer）
				ObjectMapper om = new ObjectMapper();
				Map<String, Object> map = om.readValue(json, Map.class);
				if (map.containsKey("userId")) {
					User user = om.readValue(json, User.class);
					System.out.println("退出登录的user："+user.toString());
					redisDao.delete("user-"+user.getUserId().toString());
				}
				else if (map.containsKey("customerId")) {
					Customer customer = om.readValue(json, Customer.class);
					System.out.println("退出登录的customer："+customer.toString());
					redisDao.delete("customer-"+customer.getCustomerId().toString());
				}
				rb = new ResponseBean<>("已退出登录");
			}
			else {
				rb = new ResponseBean<>(500, "无相应令牌");
			}
		} catch (Exception e) {
			System.out.println("Exception happened: "+e.getMessage());
			rb = new ResponseBean<>(500, e.getMessage());
		}
		return rb;
	}

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

	@PostMapping("/pageByNurseId")
	public PageResponseBean<List<Customer>> pageByNurseId(@RequestBody Map<String, Object> request){
		// int current, int size, String name, int customerType
		int current = (int) request.get("current"); //当前页面
		int size = (int) request.get("size"); //一页的行数
		int nurseId = (int) request.get("nurseId");
		String name = (String) request.get("name");

		IPage<Customer> page = new Page<>(current,size);

		QueryWrapper<Customer> qw = new QueryWrapper<>();
		qw.eq("nurse_id", nurseId);
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

		if (customerMapper.getByIdCard(idCard) != null) {
			return new ResponseBean<>(500, "身份证号不能与其他客户重复");
		}
		if (customerMapper.getByPhoneNumber(phoneNumber) != null) {
			return new ResponseBean<>(500, "手机号不能与其他客户重复");
		}

		Customer customer = new Customer(0, customerType, 0, "", name, idCard, age, gender, bloodType, relative, phoneNumber, "606", roomNumber, bedNumber, checkinDate, expirationDate, phoneNumber.substring(5, 11), false);
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

		Customer check1 = customerMapper.getByIdCard(customer.getIdCard());
		if (check1 != null && check1.getCustomerId() != customer.getCustomerId()) {
			return new ResponseBean<>(500, "身份证号不能与其他客户重复");
		}
		Customer check2 = customerMapper.getByPhoneNumber(customer.getPhoneNumber());
		if (check2 != null && check2.getCustomerId() != customer.getCustomerId()) {
			return new ResponseBean<>(500, "手机号不能与其他客户重复");
		}

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

	@PostMapping("/updatePassword")
	public ResponseBean<Integer> updatePassword(@RequestBody Map<String, Object> request) {
		ResponseBean<Integer> rb = null;
		try {
			int customerId = (int) request.get("customerId");
			String password = (String) request.get("password");
			String newPassword = (String) request.get("newPassword");
			Customer customer = customerMapper.selectById(customerId);
			if (customer == null || !password.equals(customer.getPassword())) {
				return new ResponseBean<>(500, "客户不存在或密码错误");
			}
			// 前端应该做好表单校验，比如输入的新密码不能少于6位等
			customer.setPassword(newPassword);
			int result = customerMapper.updateById(customer);
			if (result > 0) {
				rb = new ResponseBean<>(result);
			} else {
				rb = new ResponseBean<>(500, "Fail to update");
			}
		} catch (Exception e) {
			rb = new ResponseBean<>(500, e.getMessage());
		}
		return rb;
	}

	@PostMapping("/setNursingLevel")
	public ResponseBean<String> setNursingLevel(@RequestBody Map<String, Object> request) {
		ResponseBean<String> rb = null;
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
			rb = new ResponseBean<>("客户护理级别设置成功");
		}else {
			rb = new ResponseBean<>(500,"护理级别设置失败");
		}
		return rb;
	}

	@PostMapping("/assignNurse")
	public ResponseBean<String> assignNurse(@RequestBody Map<String, Object> request) {
		ResponseBean<String> rb = null;
		int customerId = (int) request.get("customerId");
		int nurseId = (int) request.get("nurseId");
		Customer customer = customerMapper.selectById(customerId);
		User user = userMapper.selectById(nurseId);
		if (customer == null || user == null || user.getUserType() != 1) {
			return new ResponseBean<>(500, "客户或护工不存在");
		}

		customer.setNurseId(nurseId);
		int result = customerMapper.updateById(customer);
		if(result > 0) {
			rb = new ResponseBean<>("客户管家设置成功");
		}else {
			rb = new ResponseBean<>(500,"客户管家设置失败");
		}
		return rb;
	}

	@PostMapping("/resetNurse")
	public ResponseBean<String> resetNurse(@RequestBody Map<String, Object> request) {
		ResponseBean<String> rb = null;
		int customerId = (int) request.get("customerId");
		Customer customer = customerMapper.selectById(customerId);
		if (customer == null) {
			return new ResponseBean<>(500, "客户不存在");
		}
		customer.setNurseId(0);
		int result = customerMapper.updateById(customer);
		if(result > 0) {
			rb = new ResponseBean<>("客户已从原本的健康管家服务列表中移除");
		}else {
			rb = new ResponseBean<>(500,"移除失败");
		}
		return rb;
	}

	@PostMapping("/resetNurseBatch")
	public ResponseBean<String> resetNurseBatch(@RequestBody List<Customer> customerList) {
		ResponseBean<String> rb = null;
		try {
			for (Customer customer : customerList) {
				customer.setNurseId(0);
				int result = customerMapper.updateById(customer);
				if (result <= 0) {
					return new ResponseBean<>(500, "移除过程中失败");
				}
			}
			rb = new ResponseBean<>("移除成功，共移除"+customerList.size()+"条数据");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			rb = new ResponseBean<>(500, e.getMessage());
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
