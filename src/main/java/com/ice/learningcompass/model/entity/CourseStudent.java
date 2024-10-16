package com.ice.learningcompass.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 课程
 *
 * @TableName course_student
 */
@TableName(value = "course_student")
@Data
public class CourseStudent implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学生 id
     */
    private Long studentId;

    /**
     * 课程 id
     */
    private Long courseId;

    /**
     * 课程分数
     */
    private Integer grade;

    /**
     * 学习进度（百分比表示）
     */
    private Integer process;

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