package com.neusoft.nursingcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nursingcenter.entity.BedUsageRecord;
import com.neusoft.nursingcenter.entity.PageResponseBean;
import com.neusoft.nursingcenter.entity.ResponseBean;
import com.neusoft.nursingcenter.mapper.BedUsageRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/bedUsageRecord")
public class BedUsageRecordController {
    @Autowired
    private BedUsageRecordMapper bedUsageRecordMapper;

    @RequestMapping("/getCurrentUsingRecord")
    public ResponseBean<BedUsageRecord> getCurrentUsingRecord(@RequestBody Map<String, Object> request) {
        int customerId = (int) request.get("customerId");
        // 正常情况下，一个客户应该只有一条正在使用的床位详情
        BedUsageRecord currentUsingRecord = bedUsageRecordMapper.getCurrentUsingRecord(customerId);

        ResponseBean<BedUsageRecord> rb = null;
        if (currentUsingRecord != null) {
            rb = new ResponseBean<>(currentUsingRecord);
        } else {
            rb = new ResponseBean<>(500, "No data");
        }
        return rb;
    }

    // 无条件分页查询，只看当前页与每一页的size
    @RequestMapping("/page")
    public PageResponseBean<List<BedUsageRecord>> page(@RequestBody Map<String, Object> request) {
        int current = (int) request.get("current"); //当前页面
        int size = (int) request.get("size"); //一页的行数

        IPage<BedUsageRecord> page = new Page<>(current, size);
        QueryWrapper<BedUsageRecord> qw = new QueryWrapper<>();
        qw.eq("is_deleted", 0);
        IPage<BedUsageRecord> result = bedUsageRecordMapper.selectPage(page, qw);
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

    // 用于床位管理页的多条件组合的分页查询
    // 组合条件：床位状态、客户姓名、开始日期（入住日期）
    @RequestMapping("/pageWithConditions")
    public PageResponseBean<List<BedUsageRecord>> pageByCustomerId(@RequestBody Map<String, Object> request) {
        int status = (int) request.get("status");
        String customerName = (String) request.get("customerName"); //绑定前端搜索框值变量，默认为空字符串
        String startDate = (String) request.get("startDate"); //绑定前端日期控件值变量，默认为空字符串
        int current = (int) request.get("current"); //当前页面，前端应该有默认值
        int size = (int) request.get("size"); //一页的行数，前端应该有默认值

        IPage<BedUsageRecord> page = new Page<>(current, size);
        QueryWrapper<BedUsageRecord> qw = new QueryWrapper<>();
        qw.eq("status", status);
        qw.like("customer_name", customerName);
        qw.like("start_date", startDate);
        qw.eq("is_deleted", 0);
        IPage<BedUsageRecord> result = bedUsageRecordMapper.selectPage(page, qw);
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
        QueryWrapper<BedUsageRecord> qw = new QueryWrapper<>();
        qw.eq("is_deleted", 0);
        List<BedUsageRecord> bedUsageRecordList = bedUsageRecordMapper.selectList(null);

        ResponseBean<List<BedUsageRecord>> rb = null;
        if (bedUsageRecordList.size() > 0) {
            rb = new ResponseBean<>(bedUsageRecordList);
        } else {
            rb = new ResponseBean<>(500, "No data");
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

    // 逻辑删除
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
        String bedNumber = (String) request.get("bedNumber");
        int customerId = (int) request.get("customerId");
        String customerName = (String) request.get("customerName");
        int customerGender = (int) request.get("customerGender");
        String startDate = (String) request.get("startDate");
        String endDate = (String) request.get("endDate");
        int status = (int) request.get("status"); //一般来说新增的床位使用记录是“使用中”，也就是1
        BedUsageRecord bedUsageRecord = new BedUsageRecord(0, bedNumber, customerId, customerName, customerGender, startDate, endDate, status, false);
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
