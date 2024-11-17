package com.ice.learningcompass.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 课程资料
 *
 * @TableName course_resource
 */
@TableName(value = "course_resource")
@Data
public class CourseResource implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建教师 id
     */
    private Long teacherId;

    /**
     * 课程 id
     */
    private Long courseId;

    /**
     * 资料名称
     */
    private String resourceName;

    /**
     * 资料链接
     */
    private String resourceUrl;

    /**
     * 资料类型
     */
    private Integer resourceType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}