package com.neusoft.nursingcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("level_with_program")
public class LevelWithProgram {
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    private Integer levelId;

    private Integer programId;

    private Boolean isDeleted;

    public LevelWithProgram() {}

    public LevelWithProgram(Integer id, Integer levelId, Integer programId, Boolean isDeleted) {
        this.id = id;
        this.levelId = levelId;
        this.programId = programId;
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
