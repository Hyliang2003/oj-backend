package com.liang.OJbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liang.OJbackend.model.entity.Question;
import com.liang.OJbackend.service.QuestionService;
import com.liang.OJbackend.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author 35282
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-06-04 20:39:06
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    @Override
    public void validQuestion(Question question, boolean b) {


    }
}




