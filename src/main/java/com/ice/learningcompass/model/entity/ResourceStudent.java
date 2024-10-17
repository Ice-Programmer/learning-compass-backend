package com.ice.learningcompass.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 学生资料查看记录
 * @TableName resource_student
 */
@TableName(value ="resource_student")
@Data
public class ResourceStudent implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资料 id
     */
    private Long resourceId;

    /**
     * 查看学生 id
     */
    private Long studentId;

    /**
     * 查看次数
     */
    private Integer viewNum;

    /**
     * 查看时间
     */
    private Date viewTime;

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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}