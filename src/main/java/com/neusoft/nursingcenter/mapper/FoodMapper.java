package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.Food;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {
    @Select("select * from food where type=#{type}")
    List<Food> listByType(String type);

    @Select("select * from food where name=#{name}")
    Food getByName(String name);
}
