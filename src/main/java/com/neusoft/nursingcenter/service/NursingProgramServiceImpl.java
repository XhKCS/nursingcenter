package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.LevelWithProgram;
import com.neusoft.nursingcenter.entity.NursingProgram;
import com.neusoft.nursingcenter.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NursingProgramServiceImpl implements NursingProgramService{
    @Autowired
    private NursingProgramMapper nursingProgramMapper;
    @Autowired
    private LevelWithProgramMapper levelWithProgramMapper;
    @Autowired
    private NursingRecordMapper nursingRecordMapper;
    @Autowired
    private CustomerNursingServiceMapper customerNursingServiceMapper;


    @Override
    @Transactional
    // 护理项目信息的更新要同步更新护理记录以及客户护理服务
    public int updateProgram(NursingProgram updatedProgram) {
       NursingProgram check = nursingProgramMapper.selectById(updatedProgram.getId());
       if (check == null) {
           throw new RuntimeException("不存在该id的项目");
       }
       if (check.getStatus()>0 && updatedProgram.getStatus()==0) { //状态被改为停用
           List<LevelWithProgram> list = levelWithProgramMapper.listByProgramId(updatedProgram.getId());
           if (list.size() > 0) {
               // 要尝试先删除所有该项目对应的级别-项目关系，保证护理级别的项目列表下都是可用项目
               int res1 = levelWithProgramMapper.deleteAllByProgramId(updatedProgram.getId());
               if (res1 <= 0) {
                   throw new RuntimeException("尝试删除该项目的护理级别关系时出错");
               }
           }
       }
       if (!nursingRecordMapper.listByProgramId(updatedProgram.getId()).isEmpty()) {
           if (nursingRecordMapper.updateByNursingProgram(updatedProgram) <= 0) {
               throw new RuntimeException("尝试同步更新护理记录时出错");
           }
       }
       if (!customerNursingServiceMapper.listByProgramId(updatedProgram.getId()).isEmpty()) {
           if (customerNursingServiceMapper.updateByNursingProgram(updatedProgram) <= 0) {
               throw new RuntimeException("尝试同步更新客户护理服务时出错");
           }
       }

       // 返回最终更新结果
       return nursingProgramMapper.updateById(updatedProgram);
    }

    @Override
    @Transactional
    public int deleteProgramById(int id) {
        NursingProgram nursingProgram = nursingProgramMapper.selectById(id);
        if (nursingProgram == null) {
            throw new RuntimeException("不存在该id的项目");
        }
        List<LevelWithProgram> list = levelWithProgramMapper.listByProgramId(id);
        if (list.size() > 0) {
            // 要尝试先删除所有该项目对应的级别-项目关系，保证护理级别的项目列表下都是可用项目
            int res1 = levelWithProgramMapper.deleteAllByProgramId(id);
            if (res1 <= 0) {
                throw new RuntimeException("尝试删除该项目的护理级别关系时出错");
            }
        }
        // 返回最终更新结果
        nursingProgram.setDeleted(true);
        return nursingProgramMapper.updateById(nursingProgram);
    }
}
