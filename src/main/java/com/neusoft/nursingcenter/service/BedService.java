package com.neusoft.nursingcenter.service;

import com.neusoft.nursingcenter.entity.Bed;

import java.util.List;
import java.util.Map;

public interface BedService {
    Map<String, List<Bed>> listByFloor(int floor);


}
