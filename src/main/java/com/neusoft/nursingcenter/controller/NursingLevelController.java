package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.LevelWithProgramMapper;
import com.neusoft.nursingcenter.mapper.NursingLevelMapper;
import com.neusoft.nursingcenter.mapper.NursingProgramMapper;
import com.neusoft.nursingcenter.service.NursingLevelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/nursingLevel")
public class NursingLevelController {
    @Autowired
    NursingLevelMapper nursingLevelMapper;

    @Autowired
    NursingLevelServiceImpl nursingLevelService;

    @PostMapping("/listProgramsByLevelId")
    public ResponseBean<List<NursingProgram>> listProgramsByLevelId(@RequestBody Map<String, Object> request) {
        int levelId = (int) request.get("levelId");
        List<NursingProgram> programList = nursingLevelService.listProgramsByLevelId(levelId);
        ResponseBean<List<NursingProgram>> rb = null;

        if (programList.size() > 0) {
            rb = new ResponseBean<>(programList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @PostMapping("/listProgramsByLevelName")
    public ResponseBean<List<NursingProgram>> listProgramsByLevelName(@RequestBody Map<String, Object> request) {
        String levelName = (String) request.get("levelName");
        NursingLevel nursingLevel = nursingLevelMapper.getByName(levelName);
        ResponseBean<List<NursingProgram>> rb = null;
        if (nursingLevel == null) {
            rb = new ResponseBean<>(500, "不存在该名称的护理级别");
        }
        List<NursingProgram> programList = nursingLevelService.listProgramsByLevelId(nursingLevel.getId());

        if (programList.size() > 0) {
            rb = new ResponseBean<>(programList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    // 护理级别不需要多条件分页查询，只用按状态分即可
    @PostMapping("/pageByStatus")
    public PageResponseBean<List<NursingLevel>> pageByStatus(@RequestBody Map<String, Object> request) {
        int status = (int) request.get("status");
        // current和size如果用Long类型会报错，因为number自动被识别成Integer
        int current = (int) request.get("current"); //当前页面
        int size = (int) request.get("size"); //一页的行数
        // 筛选条件
        QueryWrapper<NursingLevel> qw = new QueryWrapper<>();
        qw.eq("status", status);

        IPage<NursingLevel> page = new Page<>(current, size);
        IPage<NursingLevel> result = nursingLevelMapper.selectPage(page, qw);
        List<NursingLevel> levelList = result.getRecords();
        long total = result.getTotal();
        PageResponseBean<List<NursingLevel>> rb = null;

        if (total > 0) {
            rb = new PageResponseBean<>(levelList);
            rb.setTotal(total);
        } else {
            rb = new PageResponseBean<>(500, "No data");
        }
        return rb;
    }

    @PostMapping("/listByStatus")
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

    @PostMapping("/pageAll")
    public PageResponseBean<List<NursingLevel>> pageAll(@RequestBody Map<String, Object> request) {
        int current = (int) request.get("current"); //当前页面
        int size = (int) request.get("size"); //一页的行数

        IPage<NursingLevel> page = new Page<>(current, size);
        IPage<NursingLevel> result = nursingLevelMapper.selectPage(page, null);
        List<NursingLevel> levelList = result.getRecords();
        long total = result.getTotal();
        PageResponseBean<List<NursingLevel>> rb = null;

        if (total > 0) {
            rb = new PageResponseBean<>(levelList);
            rb.setTotal(total);
        } else {
            rb = new PageResponseBean<>(500, "No data");
        }
        return rb;
    }

    @PostMapping("/listAll")
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

    @PostMapping("/getById")
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

    @PostMapping("/getByName")
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

    @PostMapping("/add")
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

    @PostMapping("/update")
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
