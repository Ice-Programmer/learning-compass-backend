package com.ice.learningcompass.model.vo;

import com.ice.learningcompass.model.entity.CourseResource;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程资料 VO
 */
@Data
public class ResourceStudentVO implements Serializable {
    /**
     * id
     */
    private Long id;

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
     * 阅读次数
     */
    private Integer viewNum;

    /**
     * 阅读时间
     */
    private Date viewTime;

    /**
     * 是否阅读
     */
    private Boolean isRead;

    /**
     * 对象转包装类
     *
     * @param courseResource 课程资料
     * @return 课程资料包装类
     */
    public static ResourceStudentVO objToVo(CourseResource courseResource) {
        if (courseResource == null) {
            return null;
        }
        ResourceStudentVO resourceStudentVO = new ResourceStudentVO();
        BeanUtils.copyProperties(courseResource, resourceStudentVO);
        return resourceStudentVO;
    }

    private static final long serialVersionUID = 1L;
}