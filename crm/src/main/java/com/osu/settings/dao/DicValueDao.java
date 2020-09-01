package com.osu.settings.dao;

import com.osu.settings.entity.DicValue;

import java.util.List;

public interface DicValueDao {

    List<DicValue> getListByCode(String code);
}
