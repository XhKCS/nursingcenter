package com.neusoft.nursingcenter.controller;

import com.neusoft.nursingcenter.entity.NursingProgram;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.NursingProgramMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nursingProgram")
public class NursingProgramController {
    @Autowired
    private NursingProgramMapper nursingProgramMapper;

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
}
