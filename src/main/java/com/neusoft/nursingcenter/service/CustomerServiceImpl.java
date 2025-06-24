package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.Bed;
import com.neusoft.nursingcenter.entity.BedUsageRecord;
import com.neusoft.nursingcenter.entity.Customer;
import com.neusoft.nursingcenter.entity.NursingLevel;
import com.neusoft.nursingcenter.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private BedUsageRecordMapper bedUsageRecordMapper;

	@Autowired
	private BedMapper bedMapper;

	@Autowired
	private CustomerNursingServiceMapper customerNursingServiceMapper;

	@Autowired
	private NursingLevelMapper nursingLevelMapper;

	@Override
	@Transactional
	public int deleteCustomerById(int customerId) {
		// 先尝试找到该客户正在使用的床位使用记录
		BedUsageRecord currentRecord = bedUsageRecordMapper.getCurrentUsingRecord(customerId);
		if (currentRecord == null) {
			// 说明该客户当前没有用床位，那直接删除即可
			Customer customer = customerMapper.selectById(customerId);
			if (customer == null) {
				return -1;
			}
			customer.setDeleted(true);
			return customerMapper.updateById(customer);
		}
		// 先将对应床位状态更改为空闲
		Bed usingBed = bedMapper.getBedByNumber(currentRecord.getBedNumber());
		usingBed.setStatus(0); //空闲状态
		int res1 = bedMapper.updateById(usingBed);
		// 将记录逻辑删除
		currentRecord.setDeleted(true);
		int res2 = bedUsageRecordMapper.updateById(currentRecord);
		if (res1 <= 0 || res2 <= 0) {
			throw new RuntimeException("删除过程中失败");
		}
		Customer customer = customerMapper.selectById(customerId);
		customer.setDeleted(true);
		return customerMapper.updateById(customer);
	}

	// 如果对客户床位信息做了修改，要保证在该方法之前已经完成对旧床位信息的处理
	// 该方法只会对当前正在使用的床位信息做同步修改
	//如果清除了客户的护理级别，则要将原级别下的客户护理项目服务都清除
	@Override
	@Transactional
	public int updateCustomer(Customer updatedCustomer) {
		// 先尝试找到该客户正在使用的床位使用记录
		BedUsageRecord currentRecord = bedUsageRecordMapper.getCurrentUsingRecord(updatedCustomer.getCustomerId());
		Customer dbCustomer = customerMapper.selectById(updatedCustomer.getCustomerId());
		NursingLevel nursingLevel = nursingLevelMapper.getByName(dbCustomer.getNursingLevelName());

		if (currentRecord == null && nursingLevel == null) {
			// 说明该客户当前没有用床位，且原本就没有护理级别，那直接修改即可
			return customerMapper.updateById(updatedCustomer);
		}
		// 先修改对应床位记录的开始时间和结束时间（退住时间）
		currentRecord.setStartDate(updatedCustomer.getCheckinDate());
		currentRecord.setEndDate(updatedCustomer.getExpirationDate());
		int res1 = bedUsageRecordMapper.updateById(currentRecord);
		if (res1 <= 0) {
			throw new RuntimeException("修改过程中失败");
		}

		NursingLevel newLevel = nursingLevelMapper.getByName(updatedCustomer.getNursingLevelName());
		//如果清除了客户的护理级别，则要将原级别下的客户护理项目服务都清除
		if (nursingLevel != null && (newLevel == null || newLevel.getId() != nursingLevel.getId())) {
			int res2 = customerNursingServiceMapper.deleteByCustomerIdAndLevelId(updatedCustomer.getCustomerId(), nursingLevel.getId());
			if (res2 <= 0) {
				throw new RuntimeException("修改过程中失败");
			}
		}
		return customerMapper.updateById(updatedCustomer);
	}

	// 添加客户时需要自动添加对应的床位记录
	@Override
	@Transactional
	public int addCustomer(Customer customer) {
		// 先尝试添加客户
		int res1 = customerMapper.insert(customer);
		if (res1 <= 0) {
			throw new RuntimeException("添加客户的过程中失败");
//			return res1;
		}
		// 从数据库拿到添加后的客户（已经有了customerId）
		Customer dbCustomer = customerMapper.selectById(customer.getCustomerId());
		// 再添加对应床位使用详情
		BedUsageRecord currentUsingRecord = new BedUsageRecord(0, customer.getBedNumber(), customer.getCustomerId(),
				customer.getName(), customer.getGender(), customer.getCheckinDate(), customer.getExpirationDate(), 1, false);
		int res2 = bedUsageRecordMapper.insert(currentUsingRecord);
		if (res2 <= 0) {
			return res2;
		}
		return res1 + res2;
	}
}
