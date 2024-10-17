package com.ice.learningcompass.model.dto.resourcestudent;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生访问课程资料请求
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/10/17 19:42
 */
@Data
public class StudentViewResourceRequest implements Serializable {

    private Long resourceId;

}
