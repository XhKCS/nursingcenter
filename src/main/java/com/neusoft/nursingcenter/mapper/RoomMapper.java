package com.neusoft.nursingcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.nursingcenter.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomMapper extends BaseMapper<Room> {
    @Select("select * from room where room_number=#{roomNumber}")
    Room getRoomByNumber(String roomNumber);

    @Select("select * from room where floor=#{floor}")
    List<Room> listRoomsByFloor(int floor);
}
