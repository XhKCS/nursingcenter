package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neusoft.nursingcenter.entity.Bed;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.BedMapper;
import com.neusoft.nursingcenter.service.BedService;
import com.neusoft.nursingcenter.util.WebSocket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/bed")
public class BedController {
    @Autowired
    private BedMapper bedMapper;

    @Autowired
    private BedService bedService;

//    @Resource
//    private WebSocket webSocket;

    @PostMapping("/getByNumber")
    @Parameters({
            @Parameter(name = "bedNumber", description = "床位号", required =true),
    })
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

    @PostMapping("/getById")
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
    @Operation(summary = "获取一个楼层的所有床位")
    @PostMapping("/listByFloor")
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

    @PostMapping("/listCounts")
    public ResponseBean<Map<Integer, Long>> listCounts() {
        Map<Integer, Long> result = new HashMap<>();
        for (int i=0; i < 3; i++) {
            QueryWrapper<Bed> qw = new QueryWrapper<>();
            qw.eq("status", i);
            long count = bedMapper.selectCount(qw);
            result.put(i, count);
        }
        return new ResponseBean<>(result);
    }

    @PostMapping("/getAllCount")
    public ResponseBean<Long> getAllCount() {
        long count = bedMapper.selectCount(null);
        return new ResponseBean<>(count);
    }

    @PostMapping("/getCountByStatus")
    public ResponseBean<Long> getCountByStatus(@RequestBody Map<String, Object> request) {
        int status = (int) request.get("status");
        QueryWrapper<Bed> qw = new QueryWrapper<>();
        qw.eq("status", status);
        long count = bedMapper.selectCount(qw);
        return new ResponseBean<>(count);
    }

    // 获取指定房间内所有床位
    @Operation(summary = "获取指定房间内所有床位")
    @PostMapping("/listByRoomId")
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

    @PostMapping("/listByRoomNumber")
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
    @Operation(summary = "获取指定房间内空闲的床位")
    @PostMapping("/listSpareByRoomNumber")
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
    @Operation(summary = "获取某个状态的所有床位：0-空闲 / 1-外出 / 2-有人")
    @PostMapping("/listByStatus")
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
