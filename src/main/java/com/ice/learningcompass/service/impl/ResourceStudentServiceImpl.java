package com.ice.learningcompass.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.learningcompass.common.ErrorCode;
import com.ice.learningcompass.exception.ThrowUtils;
import com.ice.learningcompass.mapper.CourseResourceMapper;
import com.ice.learningcompass.model.entity.CourseResource;
import com.ice.learningcompass.model.entity.ResourceStudent;
import com.ice.learningcompass.service.ResourceStudentService;
import com.ice.learningcompass.mapper.ResourceStudentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenjiahan
 * @description 针对表【resource_student(学生资料查看记录)】的数据库操作Service实现
 * @createDate 2024-10-17 19:32:01
 */
@Service
public class ResourceStudentServiceImpl extends ServiceImpl<ResourceStudentMapper, ResourceStudent>
        implements ResourceStudentService {

    @Resource
    private CourseResourceMapper courseResourceMapper;

    @Override
    public Integer studentViewResource(Long resourceId, Long studentId) {
        // 1. 判断是否有存在浏览记录
        ResourceStudent resourceStudentRecord = baseMapper.selectOne(Wrappers.<ResourceStudent>lambdaQuery()
                .eq(ResourceStudent::getResourceId, resourceId)
                .eq(ResourceStudent::getStudentId, studentId)
                .select(ResourceStudent::getViewNum, ResourceStudent::getId)
                .last("limit 1"));

        // 如果存在浏览记录，浏览记录 +1
        if (resourceStudentRecord != null) {
            resourceStudentRecord.setViewNum(resourceStudentRecord.getViewNum() + 1);

            baseMapper.updateById(resourceStudentRecord);
            // 返回当前浏览次数
            return resourceStudentRecord.getViewNum();
        }

        // 判断课程资料是否存在
        boolean exists = courseResourceMapper.exists(Wrappers.<CourseResource>lambdaQuery()
                .eq(CourseResource::getId, resourceId));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "Resource does not exist!");
        ResourceStudent resourceStudent = new ResourceStudent();
        resourceStudent.setStudentId(studentId);
        resourceStudent.setViewNum(1);
        resourceStudent.setResourceId(resourceId);
        baseMapper.insert(resourceStudent);
        return resourceStudent.getViewNum();
    }
}




