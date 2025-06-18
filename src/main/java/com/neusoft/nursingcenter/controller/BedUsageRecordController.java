package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.BedUsageRecord;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.BedUsageRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bedUsageRecord")
public class BedUsageRecordController {
    @Autowired
    private BedUsageRecordMapper bedUsageRecordMapper;

    @RequestMapping("/getCurrentUsingRecord")
    public ResponseBean<BedUsageRecord> getCurrentUsingRecord(@RequestBody Map<String, Object> request) {
        int customerId = (int) request.get("customerId");
        BedUsageRecord bedUsageRecord = bedUsageRecordMapper.getCurrentUsingRecord(customerId);

        ResponseBean<BedUsageRecord> rb = null;
        if (bedUsageRecord != null) {
            rb = new ResponseBean<>(bedUsageRecord);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/page")
    public PageResponseBean<List<BedUsageRecord>> page(@RequestBody Map<String, Object> request) {
        Long current = (Long) request.get("current"); //当前页面
        Long size = (Long) request.get("size"); //一页的行数

        IPage<BedUsageRecord> page = new Page<>(current, size);
        IPage<BedUsageRecord> result = bedUsageRecordMapper.selectPage(page, null);
        List<BedUsageRecord> list = result.getRecords();
        long total = result.getTotal();
        PageResponseBean<List<BedUsageRecord>> rb = null;

        if (total > 0) {
            rb = new PageResponseBean<>(list);
            rb.setTotal(total);
        } else {
            rb = new PageResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/listAll")
    public ResponseBean<List<BedUsageRecord>> listAll() {
        List<BedUsageRecord> bedUsageRecordList = bedUsageRecordMapper.selectList(null);

        ResponseBean<List<BedUsageRecord>> rb = null;
        if (bedUsageRecordList.size() > 0) {
            rb = new ResponseBean<>(bedUsageRecordList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/pageByCustomerId")
    public PageResponseBean<List<BedUsageRecord>> pageByCustomerId(@RequestBody Map<String, Object> request) {
        int customerId = (int) request.get("customerId");
        Long current = (Long) request.get("current"); //当前页面
        Long size = (Long) request.get("size"); //一页的行数

        IPage<BedUsageRecord> page = new Page<>(current, size);
        QueryWrapper<BedUsageRecord> qw = new QueryWrapper<>();
        qw.eq("customer_id", customerId);
        IPage<BedUsageRecord> result = bedUsageRecordMapper.selectPage(page, null);
        List<BedUsageRecord> list = result.getRecords();
        long total = result.getTotal();
        PageResponseBean<List<BedUsageRecord>> rb = null;

        if (total > 0) {
            rb = new PageResponseBean<>(list);
            rb.setTotal(total);
        } else {
            rb = new PageResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/listByCustomerId")
    public ResponseBean<List<BedUsageRecord>> listByCustomerId(@RequestBody Map<String, Object> request) {
        int customerId = (int) request.get("customerId");
        List<BedUsageRecord> bedUsageRecordList = bedUsageRecordMapper.listByCustomerId(customerId);

        ResponseBean<List<BedUsageRecord>> rb = null;
        if (bedUsageRecordList.size() > 0) {
            rb = new ResponseBean<>(bedUsageRecordList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/listByCustomerIdAndStatus")
    public ResponseBean<List<BedUsageRecord>> listByCustomerIdAndStatus(@RequestBody Map<String, Object> request) {
        int customerId = (int) request.get("customerId");
        int status = (int) request.get("status");
        List<BedUsageRecord> bedUsageRecordList = bedUsageRecordMapper.listByCustomerIdAndStatus(customerId, status);

        ResponseBean<List<BedUsageRecord>> rb = null;
        if (bedUsageRecordList.size() > 0) {
            rb = new ResponseBean<>(bedUsageRecordList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("/listByStatus")
    public ResponseBean<List<BedUsageRecord>> listByStatus(@RequestBody Map<String, Object> request) {
        int status = (int) request.get("status");
        List<BedUsageRecord> bedUsageRecordList = bedUsageRecordMapper.listByStatus(status);

        ResponseBean<List<BedUsageRecord>> rb = null;
        if (bedUsageRecordList.size() > 0) {
            rb = new ResponseBean<>(bedUsageRecordList);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    @RequestMapping("deleteById")
    public ResponseBean<String> deleteById(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        int result = bedUsageRecordMapper.deleteById(id);

        ResponseBean<String> rb = null;
        if (result > 0) {
            rb = new ResponseBean<>("删除成功");
        } else {
            rb = new ResponseBean<>(500, "删除失败");
        }
        return rb;
    }

    @RequestMapping("add")
    public ResponseBean<String> addRecord(@RequestBody Map<String, Object> request) {
        int bedId = (int) request.get("bedId");
        int customerId = (int) request.get("customerId");
        String startDate = (String) request.get("startDate");
        String endDate = (String) request.get("endDate");
        int status = (int) request.get("status"); //一般来说新增的床位使用记录是“使用中”，也就是1
        BedUsageRecord bedUsageRecord = new BedUsageRecord(0, bedId, customerId, startDate, endDate, status, false);
        int result = bedUsageRecordMapper.insert(bedUsageRecord);

        ResponseBean<String> rb = null;
        if (result > 0) {
            rb = new ResponseBean<>("添加成功");
        } else {
            rb = new ResponseBean<>(500, "添加失败");
        }
        return rb;
    }

    @RequestMapping("/update")
    public ResponseBean<String> updateRecord(@RequestBody Map<String, Object> request) {
        int id = (int) request.get("id");
        // 一般只能修改下面两条属性
        String endDate = (String) request.get("endDate");
        int status = (int) request.get("status"); //一般来说新增的床位使用记录是“使用中”，也就是1
        ResponseBean<String> rb = null;
        BedUsageRecord bedUsageRecord = bedUsageRecordMapper.selectById(id);
        if (bedUsageRecord == null) {
            rb = new ResponseBean<>(500, "不存在该id的记录");
            return rb;
        }

        bedUsageRecord.setEndDate(endDate);
        bedUsageRecord.setStatus(status);
        int result = bedUsageRecordMapper.updateById(bedUsageRecord);
        if (result > 0) {
            rb = new ResponseBean<>("修改成功");
        } else {
            rb = new ResponseBean<>(500, "修改失败");
        }
        return rb;
    }

}
