package com.liang.OJbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.liang.OJbackend.annotation.AuthCheck;
import com.liang.OJbackend.common.BaseResponse;
import com.liang.OJbackend.common.DeleteRequest;
import com.liang.OJbackend.common.ErrorCode;
import com.liang.OJbackend.common.ResultUtils;
import com.liang.OJbackend.constant.UserConstant;
import com.liang.OJbackend.exception.BusinessException;
import com.liang.OJbackend.exception.ThrowUtils;
import com.liang.OJbackend.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.liang.OJbackend.model.dto.questionSubmit.QuestionSubmitEditRequest;
import com.liang.OJbackend.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.liang.OJbackend.model.dto.questionSubmit.QuestionSubmitUpdateRequest;
import com.liang.OJbackend.model.entity.QuestionSubmit;
import com.liang.OJbackend.model.entity.User;
import com.liang.OJbackend.model.vo.QuestionSubmitVO;
import com.liang.OJbackend.service.QuestionSubmitService;
import com.liang.OJbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
  
 */
@RestController
@RequestMapping("/questionSubmit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest, HttpServletRequest request) {
        if (questionSubmitAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitAddRequest, questionSubmit);
        List<String> tags = questionSubmitAddRequest.getTags();
        if (tags != null) {
            questionSubmit.setTags(GSON.toJson(tags));
        }
        questionSubmitService.validQuestionSubmit(questionSubmit, true);
        User loginUser = userService.getLoginUser(request);
        questionSubmit.setUserId(loginUser.getId());
        questionSubmit.setFavourNum(0);
        questionSubmit.setThumbNum(0);
        boolean result = questionSubmitService.save(questionSubmit);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionSubmitId = questionSubmit.getId();
        return ResultUtils.success(newQuestionSubmitId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestionSubmit(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        QuestionSubmit oldQuestionSubmit = questionSubmitService.getById(id);
        ThrowUtils.throwIf(oldQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestionSubmit.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = questionSubmitService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionSubmitUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestionSubmit(@RequestBody QuestionSubmitUpdateRequest questionSubmitUpdateRequest) {
        if (questionSubmitUpdateRequest == null || questionSubmitUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitUpdateRequest, questionSubmit);
        List<String> tags = questionSubmitUpdateRequest.getTags();
        if (tags != null) {
            questionSubmit.setTags(GSON.toJson(tags));
        }
        // 参数校验
        questionSubmitService.validQuestionSubmit(questionSubmit, false);
        long id = questionSubmitUpdateRequest.getId();
        // 判断是否存在
        QuestionSubmit oldQuestionSubmit = questionSubmitService.getById(id);
        ThrowUtils.throwIf(oldQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = questionSubmitService.updateById(questionSubmit);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionSubmitVO> getQuestionSubmitVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionSubmit = questionSubmitService.getById(id);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVO(questionSubmit, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitVOByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                       HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionSubmitVO>> listMyQuestionSubmitVOByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                         HttpServletRequest request) {
        if (questionSubmitQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        questionSubmitQueryRequest.setUserId(loginUser.getId());
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, request));
    }

    // endregion

    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/search/page/vo")
    public BaseResponse<Page<QuestionSubmitVO>> searchQuestionSubmitVOByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                         HttpServletRequest request) {
        long size = questionSubmitQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.searchFromEs(questionSubmitQueryRequest);
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, request));
    }

    /**
     * 编辑（用户）
     *
     * @param questionSubmitEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestionSubmit(@RequestBody QuestionSubmitEditRequest questionSubmitEditRequest, HttpServletRequest request) {
        if (questionSubmitEditRequest == null || questionSubmitEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitEditRequest, questionSubmit);
        List<String> tags = questionSubmitEditRequest.getTags();
        if (tags != null) {
            questionSubmit.setTags(GSON.toJson(tags));
        }
        // 参数校验
        questionSubmitService.validQuestionSubmit(questionSubmit, false);
        User loginUser = userService.getLoginUser(request);
        long id = questionSubmitEditRequest.getId();
        // 判断是否存在
        QuestionSubmit oldQuestionSubmit = questionSubmitService.getById(id);
        ThrowUtils.throwIf(oldQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestionSubmit.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = questionSubmitService.updateById(questionSubmit);
        return ResultUtils.success(result);
    }

}
