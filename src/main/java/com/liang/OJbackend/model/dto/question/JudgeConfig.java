package com.liang.OJbackend.model.dto.question;

import lombok.Data;

@Data
public class JudgeConfig {

    /**
     * 时间限制
     */
    private Long timeLimit;

    /**
     * 内存限制
     */
    private Integer memoryLimit;

    /**
     * 堆载限制
     */
    private Integer stackLimit;

}
