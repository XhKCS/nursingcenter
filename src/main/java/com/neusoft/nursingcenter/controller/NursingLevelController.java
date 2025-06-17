package com.neusoft.nursingcenter.controller;

import com.neusoft.nursingcenter.entity.NursingLevel;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.entity.User;
import com.neusoft.nursingcenter.mapper.NursingLevelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nursingLevel")
public class NursingLevelController {
    @Autowired
    NursingLevelMapper nursingLevelMapper;

    @RequestMapping("/listAll")
    public ResponseBean<List<NursingLevel>> listAll() {
        List<NursingLevel> userList = nursingLevelMapper.selectList(null);
        ResponseBean<List<NursingLevel>> rb = null;

        if (userList.size() > 0) {
            rb = new ResponseBean<>(userList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("getById")
    public ResponseBean<NursingLevel> getById(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        NursingLevel nursingLevel = nursingLevelMapper.selectById(id);

        ResponseBean<NursingLevel> rb = null;
        if (nursingLevel != null) {
            rb = new ResponseBean<>(nursingLevel);
        } else {
            rb = new ResponseBean<>(500, "数据库中没有该id的护理级别");
        }
        return rb;
    }

    @RequestMapping("getByName")
    public ResponseBean<NursingLevel> getByName(@RequestBody Map<String,Object> request) {
        String name = (String) request.get("name");
        NursingLevel nursingLevel = nursingLevelMapper.getByName(name);

        ResponseBean<NursingLevel> rb = null;
        if (nursingLevel != null) {
            rb = new ResponseBean<>(nursingLevel);
        } else {
            rb = new ResponseBean<>(500, "数据库中没有该id的护理级别");
        }
        return rb;
    }
}
