package com.macro.mall.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.macro.mall.mapper.QuetionsMapper;
import com.macro.mall.model.Questions;
import com.macro.mall.service.QuestionsService;
import com.macro.mall.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionsServiceImpl implements QuestionsService {

    @Autowired
    private QuetionsMapper quetionsMapper;
    @Override
    public int addQues(Questions questions) {
        questions.setId(UUID.getUUID());
        questions.setQueType(1);//目前默认选择题，后期有需求再放开
        int qt =  quetionsMapper.insert(questions);
        return qt;
    }

    @Override
    public int delQues(String id) {
        if(StringUtil.isEmpty(id)){
          return -1;
        }else{
            int qt =  quetionsMapper.deleteByPrimaryKey(id);
            return qt;
        }
    }

    @Override
    public int updateQues(Questions questions,String id) {
        questions.setId(id);
        int qt =  quetionsMapper.updateByPrimaryKeySelective(questions);

        return qt;
    }

    @Override
    public List<Questions> selectAllQues() {

        List<Questions> quelist =  quetionsMapper.selectAllQues();
        return quelist;
    }
}
