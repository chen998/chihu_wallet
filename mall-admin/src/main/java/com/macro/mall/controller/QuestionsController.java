package com.macro.mall.controller;

import com.macro.mall.model.Questions;
import com.macro.mall.service.QuestionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 问题管理Controller
 * Created by macro on 2018/8/30.
 */
@Controller
@Api(tags = "QuestionController",description = "答题管理")
@RequestMapping("/question")
public class QuestionsController {

    @Autowired
    private QuestionsService questionsService;


    @ApiOperation("添加问题")
    @RequestMapping(value = "/addQues", method = RequestMethod.POST)
    @ResponseBody
    public int addQues(Questions questions){
       int adt = questionsService.addQues(questions);

     return adt;
    }
    @ApiOperation("删除问题")
    @RequestMapping(value = "/delQues", method = RequestMethod.POST)
    @ResponseBody
    public int delQues(String id){
       int adt = questionsService.delQues(id);

     return adt;
    }
    @ApiOperation("修改问题")
    @RequestMapping(value = "/updateQues", method = RequestMethod.POST)
    @ResponseBody
    public int updateQues(Questions questions,String id){
        int adt = questionsService.updateQues(questions,id);
        return adt;
    }
    @ApiOperation("查询问题")
    @RequestMapping(value = "/selectAllQues", method = RequestMethod.POST)
    @ResponseBody
    public List<Questions> selectAllQues(){
        List<Questions> queList = questionsService.selectAllQues();
        return queList;
    }



}
