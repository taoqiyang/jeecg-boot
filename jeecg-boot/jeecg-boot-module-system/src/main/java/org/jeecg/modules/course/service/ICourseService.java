package org.jeecg.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.course.entity.Course;
import org.jeecg.modules.course.entity.CourseLesson;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 课程
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
public interface ICourseService extends IService<Course> {

	/**
	 * 添加一对多
	 * 
	 */
    void saveMain(Course course, List<CourseLesson> courseLessonList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
    void updateMain(Course course, List<CourseLesson> courseLessonList);
	
	/**
	 * 删除一对多
	 */
    void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
    void delBatchMain(Collection<? extends Serializable> idList);
	
}
