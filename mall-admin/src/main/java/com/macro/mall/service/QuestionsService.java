package com.macro.mall.service;

import com.macro.mall.model.Questions;

import java.util.List;

public interface QuestionsService {


    int addQues(Questions questions);

    int delQues(String id);

    int updateQues(Questions questions, String id);

    List<Questions> selectAllQues();
}
