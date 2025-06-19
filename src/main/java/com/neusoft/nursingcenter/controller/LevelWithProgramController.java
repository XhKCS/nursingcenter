package com.neusoft.nursingcenter.controller;

import com.neusoft.nursingcenter.entity.LevelWithProgram;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.LevelWithProgramMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/levelWithProgram")
public class LevelWithProgramController {
    @Autowired
    LevelWithProgramMapper levelWithProgramMapper;

    @RequestMapping("/add")
    public ResponseBean<String> add(@RequestBody Map<String, Object> request) {
        int levelId = (int) request.get("levelId");
        int programId = (int) request.get("programId");
        LevelWithProgram levelWithProgram = new LevelWithProgram(0, levelId, programId);
        int result = levelWithProgramMapper.insert(levelWithProgram);

        ResponseBean<String> rb = null;
        if (result > 0) {
            rb = new ResponseBean<>("添加成功");
        } else {
            rb = new ResponseBean<>(500, "添加失败");
        }
        return rb;
    }


}
