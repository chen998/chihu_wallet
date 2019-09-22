package com.macro.mall.mapper;

import com.macro.mall.model.UmsMember;
import com.macro.mall.model.UmsMemberExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface UmsMemberMapper {
    long countByExample(UmsMemberExample example);

    int deleteByExample(UmsMemberExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsMember record);

    int insertSelective(UmsMember record);

    List<UmsMember> selectByExample(UmsMemberExample example);

    UmsMember selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsMember record, @Param("example") UmsMemberExample example);

    int updateByExample(@Param("record") UmsMember record, @Param("example") UmsMemberExample example);

    int updateByPrimaryKeySelective(UmsMember record);

    int updateByPrimaryKey(UmsMember record);

    UmsMember selectByUserName(String username);

    int addSignUp(Map<String, Object> mpt);

    int updateSignUpMoney(Map<String, Object> mpt);

    List<Map<String, Object>> selectCurPerIsHaveSignUp(Map<String, Object> mpt);
}