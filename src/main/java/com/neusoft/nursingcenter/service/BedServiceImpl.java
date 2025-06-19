package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.Bed;
import com.neusoft.nursingcenter.entity.Room;
import com.neusoft.nursingcenter.mapper.BedMapper;
import com.neusoft.nursingcenter.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BedServiceImpl implements BedService {
    @Autowired
    private BedMapper bedMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public Map<String, List<Bed>> listByFloor(int floor) {
        List<Room> roomList = roomMapper.listRoomsByFloor(floor);
        Map<String, List<Bed>> resultMap = new HashMap<>();
        for (Room room : roomList) {
            resultMap.put(room.getRoomNumber(), bedMapper.listBedsByRoomNumber(room.getRoomNumber()));
        }
        return resultMap;
    }
}
