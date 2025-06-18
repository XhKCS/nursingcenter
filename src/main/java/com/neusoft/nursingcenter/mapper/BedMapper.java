package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.Bed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BedMapper extends BaseMapper<Bed> {
    @Select("select * from bed where room_id=#{roomId}")
    List<Bed> listBedsByRoomId(int roomId);

    @Select("select * from bed where room_number=#{roomNumber}")
    List<Bed> listBedsByRoomNumber(String roomNumber);

    // 返回指定房间内空闲的床位
    @Select("select * from bed where room_number=#{roomNumber} and status=0")
    List<Bed> listSpareBedsByRoomNumber(String roomNumber);

    // 返回某一状态（0-空闲 / 1-外出 / 2-有人）的所有床位
    @Select("select * from bed where status=#{status}")
    List<Bed> listBedsByStatus(int status);

    @Select("select * from bed where bed_number=#{bedNumber}")
    Bed getBedByNumber(String bedNumber);
}
