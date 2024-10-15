package com.ice.learningcompass.model.dto.course;

import com.ice.learningcompass.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/15 18:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -8633731064295236946L;

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 标题
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 至少有一个标签
     */
    private List<String> orTags;

    /**
     * 创建教师 id
     */
    private Long teacherId;

    /**
     * 参加学生 id
     */
    private Long signInStudentId;

}
