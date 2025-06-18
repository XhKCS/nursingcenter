package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.NursingProgram;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.NursingProgramMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nursingProgram")
public class NursingProgramController {
    @Autowired
    private NursingProgramMapper nursingProgramMapper;

    @RequestMapping("/page")
    public PageResponseBean<List<NursingProgram>> page(@RequestBody Map<String, Object> request) {
        Long current = (Long) request.get("current"); //当前页面
        Long size = (Long) request.get("size"); //一页的行数

        IPage<NursingProgram> page = new Page<>(current, size);
        IPage<NursingProgram> result = nursingProgramMapper.selectPage(page, null);
        List<NursingProgram> list = result.getRecords();
        long total = result.getTotal();
        PageResponseBean<List<NursingProgram>> rb = null;

        if (total > 0) {
            rb = new PageResponseBean<>(list);
            rb.setTotal(total);
        } else {
            rb = new PageResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/listAll")
    public ResponseBean<List<NursingProgram>> listAll() {
        List<NursingProgram> programList = nursingProgramMapper.selectList(null);

        ResponseBean<List<NursingProgram>> rb = null;
        if (programList.size() > 0) {
            rb = new ResponseBean<>(programList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/pageByStatus")
    public PageResponseBean<List<NursingProgram>> pageByStatus(@RequestBody Map<String, Object> request) {
        int status = (int) request.get("status");
        Long current = (Long) request.get("current"); //当前页面
        Long size = (Long) request.get("size"); //一页的行数

        IPage<NursingProgram> page = new Page<>(current, size);
        QueryWrapper<NursingProgram> qw = new QueryWrapper<>();
        qw.eq("status", status);
        IPage<NursingProgram> result = nursingProgramMapper.selectPage(page, null);
        List<NursingProgram> list = result.getRecords();
        long total = result.getTotal();
        PageResponseBean<List<NursingProgram>> rb = null;

        if (total > 0) {
            rb = new PageResponseBean<>(list);
            rb.setTotal(total);
        } else {
            rb = new PageResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/listByStatus")
    public ResponseBean<List<NursingProgram>> listByStatus(@RequestBody Map<String, Object> request) {
        int status = (int) request.get("status");
        List<NursingProgram> programList = nursingProgramMapper.listByStatus(status);

        ResponseBean<List<NursingProgram>> rb = null;
        if (programList.size() > 0) {
            rb = new ResponseBean<>(programList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/getById")
    public ResponseBean<NursingProgram> getById(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        NursingProgram nursingProgram = nursingProgramMapper.selectById(id);

        ResponseBean<NursingProgram> rb = null;
        if (nursingProgram != null) {
            rb = new ResponseBean<>(nursingProgram);
        } else {
            rb = new ResponseBean<>(500, "不存在该id的护理项目");
        }
        return rb;
    }

    @RequestMapping("/getByProgramCode")
    public ResponseBean<NursingProgram> getByProgramCode(@RequestBody Map<String, Object> request) {
        String programCode = (String) request.get("programCode");
        NursingProgram nursingProgram = nursingProgramMapper.getByProgramCode(programCode);

        ResponseBean<NursingProgram> rb = null;
        if (nursingProgram != null) {
            rb = new ResponseBean<>(nursingProgram);
        } else {
            rb = new ResponseBean<>(500, "不存在该编号的护理项目");
        }
        return rb;
    }

    @RequestMapping("/getByName")
    public ResponseBean<NursingProgram> getByName(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        NursingProgram nursingProgram = nursingProgramMapper.getByName(name);

        ResponseBean<NursingProgram> rb = null;
        if (nursingProgram != null) {
            rb = new ResponseBean<>(nursingProgram);
        } else {
            rb = new ResponseBean<>(500, "不存在该名称的护理项目");
        }
        return rb;
    }
}
