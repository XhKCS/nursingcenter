package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.LevelWithProgram;
import com.neusoft.nursingcenter.entity.NursingLevel;
import com.neusoft.nursingcenter.entity.NursingProgram;
import com.neusoft.nursingcenter.mapper.LevelWithProgramMapper;
import com.neusoft.nursingcenter.mapper.NursingLevelMapper;
import com.neusoft.nursingcenter.mapper.NursingProgramMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NursingLevelServiceImpl implements NursingLevelService {
    @Autowired
    NursingLevelMapper nursingLevelMapper;

    @Autowired
    LevelWithProgramMapper levelWithProgramMapper;

    @Autowired
    NursingProgramMapper nursingProgramMapper;

    @Override
    public List<NursingProgram> listProgramsByLevelId(int levelId) {
        List<NursingProgram> programList = new ArrayList<>();
        // 检查该护理级别当前是否启用
        NursingLevel nursingLevel = nursingLevelMapper.selectById(levelId);
        if (nursingLevel.getStatus() == 0) { //如果未启用就直接返回空集合
            return programList;
        }
        List<LevelWithProgram> levelWithProgramList = levelWithProgramMapper.listByLevelId(levelId);
        for (LevelWithProgram lwp : levelWithProgramList) {
            NursingProgram nursingProgram = nursingProgramMapper.selectById(lwp.getProgramId());
            programList.add(nursingProgram);
        }
        return programList;
    }
}
