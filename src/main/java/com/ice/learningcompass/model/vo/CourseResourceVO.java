package com.ice.learningcompass.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.ice.learningcompass.model.entity.Course;
import com.ice.learningcompass.model.entity.CourseResource;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * 课程资料 VO
 */
@Data
public class CourseResourceVO implements Serializable {
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
     * 对象转包装类
     *
     * @param courseResource 课程资料
     * @return 课程资料包装类
     */
    public static CourseResourceVO objToVo(CourseResource courseResource) {
        if (courseResource == null) {
            return null;
        }
        CourseResourceVO courseResourceVO = new CourseResourceVO();
        BeanUtils.copyProperties(courseResource, courseResourceVO);
        return courseResourceVO;
    }

    private static final long serialVersionUID = 1L;
}