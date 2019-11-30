package org.jeecg.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.course.entity.CourseLesson;

import java.util.List;

/**
 * @Description: 课程课时表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
public interface ICourseLessonService extends IService<CourseLesson> {

	List<CourseLesson> selectByMainId(String mainId);
}
