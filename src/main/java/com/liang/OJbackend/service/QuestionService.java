package com.liang.OJbackend.service;

import com.liang.OJbackend.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 35282
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-06-04 20:39:06
*/
public interface QuestionService extends IService<Question> {

    void validQuestion(Question question, boolean b);
}
