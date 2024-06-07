package com.liang.OJbackend.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liang.OJbackend.model.dto.question.QuestionQueryRequest;
import com.liang.OJbackend.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liang.OJbackend.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 35282
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-06-04 20:39:06
*/
public interface QuestionService extends IService<Question> {

    void validQuestion(Question question, boolean b);

    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

}
