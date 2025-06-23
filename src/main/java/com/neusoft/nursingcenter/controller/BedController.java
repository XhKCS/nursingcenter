package com.neusoft.nursingcenter.controller;

import com.neusoft.nursingcenter.entity.Bed;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.BedMapper;
import com.neusoft.nursingcenter.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/bed")
public class BedController {
    @Autowired
    private BedMapper bedMapper;

    @Autowired
    private BedService bedService;

    @RequestMapping("/getByNumber")
    public ResponseBean<Bed> getBedByNumber(@RequestBody Map<String, Object> request) {
        String bedNumber = (String) request.get("bedNumber");
        Bed bed = bedMapper.getBedByNumber(bedNumber);

        ResponseBean<Bed> rb = null;
        if (bed != null) {
            rb = new ResponseBean<>(bed);
        } else {
            rb = new ResponseBean<>(500, "不存在该床位号的床位");
        }
        return rb;
    }

    @RequestMapping("/getById")
    public ResponseBean<Bed> getBedById(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        Bed bed = bedMapper.selectById(id);

        ResponseBean<Bed> rb = null;
        if (bed != null) {
            rb = new ResponseBean<>(bed);
        } else {
            rb = new ResponseBean<>(500, "不存在该id的床位");
        }
        return rb;
    }

    // 获取一个楼层的所有床位
    @RequestMapping("/listByFloor")
    public ResponseBean<Map<String, List<Bed>>> listByFloor(@RequestBody Map<String, Object> request) {
        int floor = (int) request.get("floor");
        Map<String, List<Bed>> resultMap = bedService.listByFloor(floor);

        ResponseBean<Map<String, List<Bed>>> rb = null;
        if (resultMap.size() > 0) {
            rb = new ResponseBean<>(resultMap);
        } else  {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    // 获取指定房间内所有床位
    @RequestMapping("/listByRoomId")
    public ResponseBean<List<Bed>> listByRoomId(@RequestBody Map<String, Object> request) {
        int roomId = (int) request.get("roomId");
        List<Bed> bedList = bedMapper.listBedsByRoomId(roomId);
        ResponseBean<List<Bed>> rb = null;

        if (bedList.size() > 0) {
            rb = new ResponseBean<>(bedList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/listByRoomNumber")
    public ResponseBean<List<Bed>> listByRoomNumber(@RequestBody Map<String, Object> request) {
        String roomNumber = (String) request.get("roomNumber");
        List<Bed> bedList = bedMapper.listBedsByRoomNumber(roomNumber);
        ResponseBean<List<Bed>> rb = null;

        if (bedList.size() > 0) {
            rb = new ResponseBean<>(bedList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    // 获取指定房间内空闲的床位
    @RequestMapping("/listSpareByRoomNumber")
    public ResponseBean<List<Bed>> listSpareByRoomNumber(@RequestBody Map<String, Object> request) {
        String roomNumber = (String) request.get("roomNumber");
        List<Bed> bedList = bedMapper.listSpareBedsByRoomNumber(roomNumber);
        ResponseBean<List<Bed>> rb = null;

        if (bedList.size() > 0) {
            rb = new ResponseBean<>(bedList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    // 获取某个状态的所有床位
    @RequestMapping("/listByStatus")
    public ResponseBean<List<Bed>> listByStatus(@RequestBody Map<String, Object> request) {
        int status = (int) request.get("status");
        List<Bed> bedList = bedMapper.listBedsByStatus(status);
        ResponseBean<List<Bed>> rb = null;

        if (bedList.size() > 0) {
            rb = new ResponseBean<>(bedList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }
}
