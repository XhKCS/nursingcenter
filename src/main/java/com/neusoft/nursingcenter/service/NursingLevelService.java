package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.NursingProgram;

import java.util.List;

public interface NursingLevelService {
    List<NursingProgram> listProgramsByLevelId(int levelId);


}
