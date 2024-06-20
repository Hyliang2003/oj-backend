package com.liang.OJbackend.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionAddRequest implements Serializable {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 答案
     */
    private String answer;

    /**
     * 标签列表(json 数组)
     */
    private List<String> tags;

    /**
     * 判题用例(Json对象)
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置(Json对象)
     */
    private JudgeConfig judgeConfig;
    private static final long serialVersionUID = 1L;
}
