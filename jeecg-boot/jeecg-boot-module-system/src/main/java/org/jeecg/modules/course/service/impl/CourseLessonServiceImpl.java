package org.jeecg.modules.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.course.entity.CourseLesson;
import org.jeecg.modules.course.mapper.CourseLessonMapper;
import org.jeecg.modules.course.service.ICourseLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 课程课时表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Service
public class CourseLessonServiceImpl extends ServiceImpl<CourseLessonMapper, CourseLesson> implements ICourseLessonService {
	
	@Autowired
	private CourseLessonMapper courseLessonMapper;
	
	@Override
	public List<CourseLesson> selectByMainId(String mainId) {
		return courseLessonMapper.selectByMainId(mainId);
	}
}
