package com.liang.OJbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liang.OJbackend.constant.CommonConstant;
import com.liang.OJbackend.model.dto.question.JudgeCase;
import com.liang.OJbackend.model.dto.question.JudgeConfig;
import com.liang.OJbackend.model.dto.question.QuestionQueryRequest;
import com.liang.OJbackend.model.entity.Question;
import com.liang.OJbackend.model.entity.User;
import com.liang.OJbackend.model.vo.QuestionVO;
import com.liang.OJbackend.service.QuestionService;
import com.liang.OJbackend.mapper.QuestionMapper;
import com.liang.OJbackend.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.util.CollectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 35282
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-06-04 20:39:06
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{


    @Resource
    private UserService userService;

    @Override
    public void validQuestion(Question question, boolean b) {

    }

    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        if (question == null){
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        return questionVO;
    }

    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null){
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        if (tags != null && !tags.isEmpty()){
            for (String tag : tags) {
                queryWrapper.like("tags", '/'+tag+'/');
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId),"userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id),"id", id);
        queryWrapper.eq("isDelete",false);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        return queryWrapper;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)){
            return questionVOPage;
        }
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long,List<User>> userIdUserMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = new QuestionVO();
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserMap.containsKey(userId)){
                user = userIdUserMap.get(userId).get(0);
            }
            questionVO.setUserVO(userService.getUserVO(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }
}




