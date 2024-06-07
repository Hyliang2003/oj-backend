package com.liang.OJbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liang.OJbackend.common.ErrorCode;
import com.liang.OJbackend.exception.BusinessException;
import com.liang.OJbackend.model.dto.QuestionSubmit.QuestionSubmitAddRequest;
import com.liang.OJbackend.model.dto.QuestionSubmit.QuestionSubmitQueryRequest;
import com.liang.OJbackend.model.entity.Question;
import com.liang.OJbackend.model.entity.QuestionSubmit;
import com.liang.OJbackend.model.entity.User;
import com.liang.OJbackend.model.enums.QuestionSubmitEnum;
import com.liang.OJbackend.model.enums.QuestionSubmitStatusEnum;
import com.liang.OJbackend.model.vo.QuestionSubmitVO;
import com.liang.OJbackend.service.QuestionService;
import com.liang.OJbackend.service.QuestionSubmitService;
import com.liang.OJbackend.mapper.QuestionSubmitMapper;
import com.liang.OJbackend.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
* @author 35282
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-06-04 20:39:38
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Resource
    private QuestionService questionService;


    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        return null;
    }

    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitEnum questionSubmitEnum = QuestionSubmitEnum.getEnumByValue(language);
        if (questionSubmitEnum == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该语言暂不支持");

        }
        long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "该题目不存在");
        }


        long userId = loginUser.getId();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean result = this.save(questionSubmit);
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交失败");
        }
        long questionSubmitId = questionSubmit.getId();
        CompletableFuture.runAsync(() -> {
//            judgeService.doJudge(questionSubmitId);
        });
        return questionSubmitId;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {


        return null;
    }
}




