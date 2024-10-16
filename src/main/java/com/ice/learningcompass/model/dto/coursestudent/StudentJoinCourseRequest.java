package com.ice.learningcompass.model.dto.coursestudent;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生加入课程请求体
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/16 12:16
 */
@Data
public class StudentJoinCourseRequest implements Serializable {

    private static final long serialVersionUID = 2368368526026731212L;

    private Long courseId;

}
