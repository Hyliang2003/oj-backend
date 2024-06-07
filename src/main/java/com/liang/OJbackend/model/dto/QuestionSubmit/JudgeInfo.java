package com.liang.OJbackend.model.dto.QuestionSubmit;

import lombok.Data;

import java.io.Serializable;

@Data
public class JudgeInfo implements Serializable {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗时间
     */
    private Long time;

    /**
     * 消耗内存
     */
    private Long memory;
    private static final long serialVersionUID = 1L;

}
