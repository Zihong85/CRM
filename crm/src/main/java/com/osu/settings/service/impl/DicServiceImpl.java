package com.osu.settings.service.impl;

import com.osu.settings.dao.DicTypeDao;
import com.osu.settings.dao.DicValueDao;
import com.osu.settings.entity.DicType;
import com.osu.settings.entity.DicValue;
import com.osu.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {

    @Autowired
    private DicValueDao dicValueDao;

    @Autowired
    private DicTypeDao dicTypeDao;

    @Override
    public Map<String, List<DicValue>> getAll() {
        List<DicType> dicTypes = dicTypeDao.getTypeList();
        Map<String, List<DicValue>> map = new HashMap<>();

        for (DicType dicType : dicTypes) {
            String code = dicType.getCode();
            List<DicValue> dicValues = dicValueDao.getListByCode(code);
            map.put(code,dicValues);
        }
        return map;
    }
}
