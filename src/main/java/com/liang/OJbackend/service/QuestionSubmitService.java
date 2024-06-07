package com.liang.OJbackend.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liang.OJbackend.model.dto.QuestionSubmit.QuestionSubmitAddRequest;
import com.liang.OJbackend.model.dto.QuestionSubmit.QuestionSubmitQueryRequest;
import com.liang.OJbackend.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liang.OJbackend.model.entity.User;
import com.liang.OJbackend.model.vo.QuestionSubmitVO;

/**
* @author 35282
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-06-04 20:39:38
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {


    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
