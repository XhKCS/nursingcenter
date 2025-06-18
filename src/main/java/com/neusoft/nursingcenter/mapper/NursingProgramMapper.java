package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.NursingProgram;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NursingProgramMapper extends BaseMapper<NursingProgram> {
    @Select("select * from nursing_program where status=#{status}")
    List<NursingProgram> listByStatus(int status);

    @Select("select * from nursing_program where program_code=#{programCode}")
    NursingProgram getByProgramCode(String programCode);

    @Select("select * from nursing_program where name=#{name}")
    NursingProgram getByName(String name);
}
