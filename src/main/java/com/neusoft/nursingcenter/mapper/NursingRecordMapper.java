package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.NursingProgram;
import com.neusoft.nursingcenter.entity.NursingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NursingRecordMapper extends BaseMapper<NursingRecord> {
    @Update("update nursing_record set program_code=#{e.programCode}, program_name=#{e.name} where program_id=#{e.id}")
    int updateByNursingProgram(@Param("e") NursingProgram nursingProgram);

    @Select("select * from nursing_record where program_id=#{programId}")
    List<NursingRecord> listByProgramId(int programId);
}
