package com.ice.learningcompass.model.dto.course;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 课程修改请求体
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/15 17:30
 */
@Data
public class CourseUpdateRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片
     */
    private String picture;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tagList;

    /**
     * 课程状态：0-开启/1-关闭
     */
    private Integer status;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

}
