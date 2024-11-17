package com.ice.learningcompass.model.vo;

import cn.hutool.core.collection.CollUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ice.learningcompass.model.entity.Course;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课程视图
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/15 18:06
 */
@Data
public class CourseVO implements Serializable {

    private static final long serialVersionUID = 3329425427894990340L;

    private static final Gson GSON = new Gson();

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
    private List<String> tagList;

    /**
     * 创建教师 id
     */
    private UserVO teacherInfo;

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
     * 包装类转对象
     *
     * @param courseVO 课程包装类
     * @return 课程类
     */
    public static Course voToObj(CourseVO courseVO) {
        if (courseVO == null) {
            return null;
        }
        Course course = new Course();
        BeanUtils.copyProperties(courseVO, course);
        if (CollUtil.isNotEmpty(courseVO.getTagList())) {
            List<String> tagList = courseVO.getTagList();
            course.setTags(GSON.toJson(tagList));
        }
        return course;
    }

    /**
     * 对象转包装类
     *
     * @param course 课程
     * @return 课程包装类
     */
    public static CourseVO objToVo(Course course) {
        if (course == null) {
            return null;
        }
        CourseVO courseVO = new CourseVO();
        BeanUtils.copyProperties(course, courseVO);
        if (StringUtils.isNotBlank(course.getTags())) {
            List<String> tagList = GSON.fromJson(course.getTags(), new TypeToken<List<String>>() {
            }.getType());
            courseVO.setTagList(tagList);
        }
        return courseVO;
    }
}
