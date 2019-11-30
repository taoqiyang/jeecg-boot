package org.jeecg.modules.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.course.entity.CourseLesson;

import java.util.List;

/**
 * @Description: 课程课时表
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
public interface CourseLessonMapper extends BaseMapper<CourseLesson> {

	boolean deleteByMainId(@Param("mainId") String mainId);
    
	List<CourseLesson> selectByMainId(@Param("mainId") String mainId);
}
