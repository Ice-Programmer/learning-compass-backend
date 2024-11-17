package com.ice.learningcompass.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 课程
 *
 * @TableName course
 */
@TableName(value = "course")
@Data
public class Course implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 课程介绍
     */
    private String introduction;

    /**
     * 图片
     */
    private String picture;

    /**
     * 标签列表（json 数组）
     */
    private String tags;

    /**
     * 创建教师 id
     */
    private Long teacherId;

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