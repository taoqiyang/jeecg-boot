package org.jeecg.modules.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.course.entity.Course;
import org.jeecg.modules.course.entity.CourseLesson;
import org.jeecg.modules.course.mapper.CourseLessonMapper;
import org.jeecg.modules.course.mapper.CourseMapper;
import org.jeecg.modules.course.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 课程
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

	@Autowired
	private CourseMapper courseMapper;
	@Autowired
	private CourseLessonMapper courseLessonMapper;
	
	@Override
	@Transactional
	public void saveMain(Course course, List<CourseLesson> courseLessonList) {
		courseMapper.insert(course);
		if(courseLessonList!=null && courseLessonList.size()>0) {
			for(CourseLesson entity:courseLessonList) {
				//外键设置
				entity.setCourseId(course.getId());
				courseLessonMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(Course course,List<CourseLesson> courseLessonList) {
		courseMapper.updateById(course);
		
		//1.先删除子表数据
		courseLessonMapper.deleteByMainId(course.getId());
		
		//2.子表数据重新插入
		if(courseLessonList!=null && courseLessonList.size()>0) {
			for(CourseLesson entity:courseLessonList) {
				//外键设置
				entity.setCourseId(course.getId());
				courseLessonMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		courseLessonMapper.deleteByMainId(id);
		courseMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			courseLessonMapper.deleteByMainId(id.toString());
			courseMapper.deleteById(id);
		}
	}
	
}
