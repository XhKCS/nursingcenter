package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.NursingProgram;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NursingProgramMapper extends BaseMapper<NursingProgram> {
    @Select("select * from nursing_program where status=#{status} and is_deleted=0")
    List<NursingProgram> listByStatus(int status);

    @Select("select * from nursing_program where program_code=#{programCode} and is_deleted=0")
    NursingProgram getByProgramCode(String programCode);

    @Select("select * from nursing_program where name=#{name} and is_deleted=0")
    NursingProgram getByName(String name);

    // 注意，护理项目的移除和停用不能影响到客户已购买的护理项目，所以查客户已购买的护理项目时直接selectById即可，不用筛状态或是否已删除
}
