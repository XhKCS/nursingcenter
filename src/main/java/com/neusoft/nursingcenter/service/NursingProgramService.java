package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.NursingProgram;

public interface NursingProgramService {
    int updateProgram(NursingProgram updatedProgram);

    int deleteProgramById(int id);
}
