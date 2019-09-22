package com.macro.mall.mapper;

import com.macro.mall.model.Questions;

import java.util.List;

public interface QuetionsMapper {


    int insert(Questions questions);

    int deleteByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Questions questions);

    List<Questions> selectAllQues();
}
