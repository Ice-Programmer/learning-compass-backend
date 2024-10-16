package com.ice.learningcompass.model.dto.courseresource;

import lombok.Data;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/16 14:09
 */
@Data
public class CourseResourceAddRequest implements Serializable {

    private static final long serialVersionUID = 1286604518415904598L;

    /**
     * 课程 id
     */
    private Long courseId;

    /**
     * 资料名称
     */
    private String resourceName;

    /**
     * 资料 url
     */
    private String resourceUrl;
}
