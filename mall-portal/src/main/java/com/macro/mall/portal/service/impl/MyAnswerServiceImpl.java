package com.macro.mall.portal.service.impl;

import com.macro.mall.mapper.MyAnswerMapper;
import com.macro.mall.model.MyAnswer;
import com.macro.mall.portal.service.MyAnswerService;
import com.macro.mall.portal.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyAnswerServiceImpl  implements MyAnswerService {

    @Autowired
    private MyAnswerMapper myAnswerMapper;
    @Override
    public int add(MyAnswer myAnswer) {
        myAnswer.setId(UUID.getUUID());
        int aint = myAnswerMapper.insert(myAnswer);

        return aint;
    }
}
