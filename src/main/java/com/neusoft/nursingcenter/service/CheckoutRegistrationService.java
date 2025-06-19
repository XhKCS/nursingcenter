package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.CheckoutRegistration;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface CheckoutRegistrationService {

    PageResponseBean<List<CheckoutRegistration>> page(@RequestBody Map<String, Object> request);

    ResponseBean<Integer> update(@RequestBody CheckoutRegistration data);

}
