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

    @RequestMapping("/listByStatus")
    public ResponseBean<List<NursingLevel>> listByStatus(@RequestBody Map<String, Object> request) {
        int status = (int) request.get("status");
        List<NursingLevel> levelList = nursingLevelMapper.listByStatus(status);
        ResponseBean<List<NursingLevel>> rb = null;

        if (levelList.size() > 0) {
            rb = new ResponseBean<>(levelList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/listAll")
    public ResponseBean<List<NursingLevel>> listAll() {
        List<NursingLevel> levelList = nursingLevelMapper.selectList(null);
        ResponseBean<List<NursingLevel>> rb = null;

        if (levelList.size() > 0) {
            rb = new ResponseBean<>(levelList);
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
            rb = new ResponseBean<>(500, "数据库中没有该名称的护理级别");
        }
        return rb;
    }

    @RequestMapping("add")
    public ResponseBean<String> add(@RequestBody Map<String,Object> request) {
        String name = (String) request.get("name");
        int status = (int) request.get("status");
        NursingLevel check = nursingLevelMapper.getByName(name);
        ResponseBean<String> rb = null;

        if (check != null) {
            rb = new ResponseBean<>(500, "不能添加重名的护理级别");
            return rb;
        }

        NursingLevel nursingLevelToAdd = new NursingLevel(0, name, status);
        int result = nursingLevelMapper.insert(nursingLevelToAdd);
        if (result > 0) {
            rb = new ResponseBean<>("添加成功");
        } else {
            rb = new ResponseBean<>(500, "添加失败");
        }
        return rb;
    }

    @RequestMapping("update")
    public ResponseBean<String> update(@RequestBody Map<String,Object> request) {
        int id = (int) request.get("id");
        int status = (int) request.get("status");
        NursingLevel nursingLevelToUpdate = nursingLevelMapper.selectById(id);
        ResponseBean<String> rb = null;

        if (nursingLevelToUpdate == null) {
            rb = new ResponseBean<>(500, "不存在该id的护理级别");
            return rb;
        }

        // 只能修改状态
        nursingLevelToUpdate.setStatus(status);
        int result = nursingLevelMapper.updateById(nursingLevelToUpdate);
        if (result > 0) {
            rb = new ResponseBean<>("修改成功");
        } else {
            rb = new ResponseBean<>(500, "修改失败");
        }
        return rb;
    }
}
