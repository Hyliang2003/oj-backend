package com.liang.OJbackend.model.enums;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Getter
public enum QuestionSubmitStatusEnum {

    WAITING(0, "等待执行"),

    RUNNING(1, "正在执行"),

    SUCCEED(2, "成功"),

    FAILED(3, "失败");

    private final Integer value;

    private final String text;

    QuestionSubmitStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }
    public static QuestionSubmitStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (QuestionSubmitStatusEnum questionSubmitStatusEnum : QuestionSubmitStatusEnum.values()) {
            if (Objects.equals(questionSubmitStatusEnum.value, value)) {
                return questionSubmitStatusEnum;
            }
        }
        return null;
    }

}
