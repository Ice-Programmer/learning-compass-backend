package com.ice.learningcompass.service;

import com.ice.learningcompass.model.entity.ResourceStudent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author chenjiahan
 * @description 针对表【resource_student(学生资料查看记录)】的数据库操作Service
 * @createDate 2024-10-17 19:32:01
 */
public interface ResourceStudentService extends IService<ResourceStudent> {

    /**
     * 学生访问课程资料
     *
     * @param resourceId 资料 id
     * @param studentId  学生 id
     * @return 访问次数
     */
    Integer studentViewResource(Long resourceId, Long studentId);
}
