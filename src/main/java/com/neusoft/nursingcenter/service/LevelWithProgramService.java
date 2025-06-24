package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.NursingProgram;
import com.neusoft.nursingcenter.entity.PageResponseBean;

import java.util.List;

public interface LevelWithProgramService {
    PageResponseBean<List<NursingProgram>> pageProgramsByLevelId(int levelId, String programName, int current, int size);
}
