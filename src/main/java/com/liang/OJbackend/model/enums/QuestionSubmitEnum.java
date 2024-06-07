package com.liang.OJbackend.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum QuestionSubmitEnum {
    JAVA("java", "java"),
    CPP("cpp", "cpp"),
    PYTHON("python", "python"),
    JAVASCRIPT("javascript", "javascript"),
    GO("go", "go"),
    PHP("php", "php"),
    C("c", "c"),
    CPLUSPLUS("c++", "c++");




    private final String text;
    private final String value;

    QuestionSubmitEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }
    public static QuestionSubmitEnum getEnumByValue(String value) {
        if (value == null) {
            return null;
        }
        for (QuestionSubmitEnum questionSubmitEnum : QuestionSubmitEnum.values()) {
            if (questionSubmitEnum.value.equals(value)) {
                return questionSubmitEnum;
            }
        }
        return null;
    }

}
