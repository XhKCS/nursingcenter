package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.LevelWithProgram;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LevelWithProgramMapper extends BaseMapper<LevelWithProgram> {
    //该方法会在NursingProgramService中被调用
    @Delete("delete from level_with_program where program_id=#{programId}")
    int deleteAllByProgramId(int programId);

    @Delete("delete from level_with_program where level_id=#{levelId}")
    int deleteAllByLevelId(int levelId);

    @Select("select * from level_with_program where level_id=#{levelId} and program_id=#{programId}")
    LevelWithProgram getByLevelAndProgram(@Param("levelId") int levelId, @Param("programId") int programId);

    //该方法会在NursingLevelController中被调用，配合NursingProgramMapper一起
    @Select("select * from level_with_program where level_id=#{levelId}")
    List<LevelWithProgram> listByLevelId(int levelId);

    @Select("select * from level_with_program where program_id=#{programId}")
    List<LevelWithProgram> listByProgramId(int programId);
}
