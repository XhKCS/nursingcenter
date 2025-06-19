package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.NursingProgram;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.NursingProgramMapper;
import com.neusoft.nursingcenter.service.NursingProgramServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/nursingProgram")
public class NursingProgramController {
    @Autowired
    private NursingProgramMapper nursingProgramMapper;

    @Autowired
    private NursingProgramServiceImpl nursingProgramService;

    @RequestMapping("/pageAll")
    public PageResponseBean<List<NursingProgram>> page(@RequestBody Map<String, Object> request) {
        Long current = (Long) request.get("current"); //当前页面
        Long size = (Long) request.get("size"); //一页的行数

        IPage<NursingProgram> page = new Page<>(current, size);
        IPage<NursingProgram> result = nursingProgramMapper.selectPage(page, null);
        List<NursingProgram> list = result.getRecords();
        long total = result.getTotal();
        PageResponseBean<List<NursingProgram>> prb = null;

        if (total > 0) {
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        } else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
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

    // 多条件组合分页查询
    // 组合条件：项目名称、状态
    @RequestMapping("/page")
    public PageResponseBean<List<NursingProgram>> pageByStatus(@RequestBody Map<String, Object> request) {
        int status = (int) request.get("status");
        String name = (String) request.get("name");
        Long current = (Long) request.get("current"); //当前页面
        Long size = (Long) request.get("size"); //一页的行数

        IPage<NursingProgram> page = new Page<>(current, size);
        QueryWrapper<NursingProgram> qw = new QueryWrapper<>();
        qw.eq("status", status);
        qw.like("name", name);
        IPage<NursingProgram> result = nursingProgramMapper.selectPage(page, null);
        List<NursingProgram> list = result.getRecords();
        long total = result.getTotal();
        PageResponseBean<List<NursingProgram>> prb = null;

        if (total > 0) {
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        } else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
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

    @RequestMapping("/add")
    public ResponseBean<String> add(@RequestBody NursingProgram nursingProgram) {
        nursingProgram.setDeleted(false);
        int result = nursingProgramMapper.insert(nursingProgram);
        ResponseBean<String> rb = null;

        if (result > 0) {
            rb = new ResponseBean<>("添加成功");
        } else {
            rb = new ResponseBean<>(500, "添加失败");
        }
        return rb;
    }

    // 事务
    @RequestMapping("/update")
    public ResponseBean<String> update(@RequestBody NursingProgram updatedProgram) {
        updatedProgram.setDeleted(false); //修改不能够让项目被删除
        ResponseBean<String> rb = null;
        try {
            int result = nursingProgramService.updateProgram(updatedProgram);
            if (result > 0) {
                rb = new ResponseBean<>("修改成功");
            } else {
                rb = new ResponseBean<>(500, "修改失败");
            }
        } catch (Exception e) {
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }

    // 事务
    @RequestMapping("/deleteById")
    public ResponseBean<String> deleteById(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        ResponseBean<String> rb = null;
        try {
            int result = nursingProgramService.deleteProgramById(id);
            if (result > 0) {
                rb = new ResponseBean<>("删除成功");
            } else {
                rb = new ResponseBean<>(500, "删除失败");
            }
        } catch (Exception e) {
            rb = new ResponseBean<>(500, e.getMessage());
        }
        return rb;
    }
}
