package org.jeecg.modules.course.vo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jeecg.modules.course.entity.Course;
import org.jeecg.modules.course.entity.CourseLesson;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 课程
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@Data
public class CoursePage {
	
	/**主键*/
	private String id;
	/**课程标题*/
	@Excel(name = "课程标题", width = 15)
	private String title;
	/**课程描述*/
	@Excel(name = "课程描述", width = 15)
	private String description;
	/**封面图片*/
	@Excel(name = "封面图片", width = 15)
	private String cover;
	/**课程标签*/
	@Excel(name = "课程标签", width = 15)
	private String tags;
    private List<String> tagList;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**发布状态*/
	@Excel(name = "发布状态", width = 15)
	private Integer sendStatus;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
	private Integer delFlag;
	
	@ExcelCollection(name="课程课时表")
	private List<CourseLesson> courseLessonList;


    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
        if(tagList == null) {
            return;
        }
        this.tags = String.join(",", tagList);
    }

    public void setTags(String tags) {
        this.tags = tags;
        if(tags == null) return;
        this.tagList = Arrays.asList(tags.split(","));
    }
}
