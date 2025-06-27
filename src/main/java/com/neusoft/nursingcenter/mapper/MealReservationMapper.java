package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.config.MyBaseMapper;
import com.neusoft.nursingcenter.entity.MealReservation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MealReservationMapper extends MyBaseMapper<MealReservation> {
}
