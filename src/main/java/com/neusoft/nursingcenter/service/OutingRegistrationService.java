package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.OutingRegistration;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface OutingRegistrationService {
    PageResponseBean<List<OutingRegistration>> page(@RequestBody Map<String, Object> request);
}
