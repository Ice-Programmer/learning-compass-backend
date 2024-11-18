package com.ice.learningcompass.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子是否回复枚举
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/9/19 16:17
 */
public enum PostReplyTypeEnum {

    NOT_REPLY("原帖", 0),
    IS_REPLY("回复贴", 1);

    private final String text;

    private final Integer value;

    PostReplyTypeEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static PostReplyTypeEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (PostReplyTypeEnum anEnum : PostReplyTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
