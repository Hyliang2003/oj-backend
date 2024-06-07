package com.liang.OJbackend.model.dto.QuestionSubmit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class QuestionSubmitAddRequest implements Serializable {
    /**
     * 编程语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;

    /**
     * 判题信息
     */
    private String judgeInfo;

    /**
     * 题目 id
     */
    private Long questionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
