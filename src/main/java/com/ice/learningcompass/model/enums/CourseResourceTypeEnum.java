package com.ice.learningcompass.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程资料类型
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/9/19 16:17
 */
public enum CourseResourceTypeEnum {

    COURSE_OUTLINE("课程大纲", 0),
    COURSE_LECTURE("课程课件", 1),
    COURSE_VIDEO("课程视频", 2);

    private final String text;

    private final Integer value;

    CourseResourceTypeEnum(String text, Integer value) {
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
    public static CourseResourceTypeEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (CourseResourceTypeEnum anEnum : CourseResourceTypeEnum.values()) {
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
