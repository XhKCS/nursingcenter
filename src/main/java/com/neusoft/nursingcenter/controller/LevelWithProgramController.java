package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.Food;
import com.neusoft.nursingcenter.entity.LevelWithProgram;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.LevelWithProgramMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/levelWithProgram")
public class LevelWithProgramController {

    @Autowired
    private LevelWithProgramMapper levelWithProgramMapper;

    @PostMapping("/page")
    public PageResponseBean<List<LevelWithProgram>> page (@RequestBody Map<String, Object> request){
        int current = (int)request.get("current");
        int size = (int)request.get("size");
        Integer levelId = (Integer)request.get("levelId");

        LambdaQueryWrapper<LevelWithProgram> qw = new LambdaQueryWrapper<>();
        qw.eq(null != levelId,LevelWithProgram :: getProgramId,levelId);

        IPage<LevelWithProgram> page = new Page<>(current,size);
        IPage<LevelWithProgram> result = levelWithProgramMapper.selectPage(page,qw);

        List<LevelWithProgram> list = result.getRecords();

        long total = result.getTotal();

        PageResponseBean<List<LevelWithProgram>> prb = null;
        if(total > 0){
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
    }

    @PostMapping("/add")
    public ResponseBean<Integer> add(@RequestBody LevelWithProgram levelWithProgram) {
        Integer result = levelWithProgramMapper.insert(levelWithProgram);
        ResponseBean<Integer> rb = null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to add");
        }
        return rb;
    }

    @PostMapping("/delete")
    public ResponseBean<Integer> delete(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        Integer result = levelWithProgramMapper.deleteById(id);
        ResponseBean<Integer> rb =null;
        if(result > 0) {
            rb = new ResponseBean<>(result);
        }else {
            rb = new ResponseBean<>(500,"Fail to delete");
        }

        return rb;
    }
}
