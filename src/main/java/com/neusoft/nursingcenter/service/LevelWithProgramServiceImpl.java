package com.neusoft.nursingcenter.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.*;
import com.neusoft.nursingcenter.mapper.LevelWithProgramMapper;
import com.neusoft.nursingcenter.mapper.NursingLevelMapper;
import com.neusoft.nursingcenter.mapper.NursingProgramMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class LevelWithProgramServiceImpl implements LevelWithProgramService {
    @Autowired
    NursingLevelMapper nursingLevelMapper;

    @Autowired
    LevelWithProgramMapper levelWithProgramMapper;

    @Autowired
    NursingProgramMapper nursingProgramMapper;

    @Override
    public PageResponseBean<List<NursingProgram>> pageProgramsByLevelId(int levelId, String programName, int current, int size) {
        PageResponseBean<List<NursingProgram>> prb = null;
        // 检查该护理级别当前是否启用
        NursingLevel nursingLevel = nursingLevelMapper.selectById(levelId);
        if (nursingLevel.getStatus() == 0) { //如果未启用就直接返回空
            prb = new PageResponseBean<>(500, "No data");
            return prb;
        }

        List<LevelWithProgram> levelWithProgramList = levelWithProgramMapper.listByLevelId(levelId);
        if (levelWithProgramList.isEmpty()) {
            return new PageResponseBean<>(500, "No data");
        }
        QueryWrapper<NursingProgram> qw = new QueryWrapper<>();
        qw.like("name", programName);
        Consumer<QueryWrapper<NursingProgram>> consumer = qw2 -> {
            for (LevelWithProgram lwp : levelWithProgramList) {
                qw2.or().eq("id", lwp.getProgramId());
            }
        };
        qw.and(consumer);
        IPage<NursingProgram> page = new Page<>(current,size);
        IPage<NursingProgram> result = nursingProgramMapper.selectPage(page, qw);
        List<NursingProgram> list = result.getRecords();
        long total = result.getTotal();

        if(total > 0){
            prb = new PageResponseBean<>(list);
            prb.setTotal(total);
        }else {
            prb = new PageResponseBean<>(500, "No data");
        }
        return prb;
    }
}
