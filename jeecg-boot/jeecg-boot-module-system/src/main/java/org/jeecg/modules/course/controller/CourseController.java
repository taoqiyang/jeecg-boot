package org.jeecg.modules.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.course.entity.Course;
import org.jeecg.modules.course.entity.CourseLesson;
import org.jeecg.modules.course.service.ICourseLessonService;
import org.jeecg.modules.course.service.ICourseService;
import org.jeecg.modules.course.vo.CoursePage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

 /**
 * @Description: 课程
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@RestController
@RequestMapping("/course/course")
@Slf4j
public class CourseController {
	@Autowired
	private ICourseService courseService;
	@Autowired
	private ICourseLessonService courseLessonService;
	
	/**
	 * 分页列表查询
	 *
	 * @param course
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Course course,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Course> queryWrapper = QueryGenerator.initQueryWrapper(course, req.getParameterMap());
		Page<Course> page = new Page<Course>(pageNo, pageSize);
		IPage<Course> pageList = courseService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param coursePage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CoursePage coursePage) {
		Course course = new Course();
		BeanUtils.copyProperties(coursePage, course);
		courseService.saveMain(course, coursePage.getCourseLessonList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param coursePage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CoursePage coursePage) {
		Course course = new Course();
		BeanUtils.copyProperties(coursePage, course);
		Course courseEntity = courseService.getById(course.getId());
		if(courseEntity==null) {
			return Result.error("未找到对应数据");
		}
		courseService.updateMain(course, coursePage.getCourseLessonList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		courseService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.courseService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Course course = courseService.getById(id);
		if(course==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(course);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryCourseLessonByMainId")
	public Result<?> queryCourseLessonListByMainId(@RequestParam(name="id",required=true) String id) {
		List<CourseLesson> courseLessonList = courseLessonService.selectByMainId(id);
		return Result.ok(courseLessonList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param course
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Course course) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Course> queryWrapper = QueryGenerator.initQueryWrapper(course, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<Course> queryList = courseService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<Course> courseList = new ArrayList<Course>();
      if(oConvertUtils.isEmpty(selections)) {
          courseList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          courseList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<CoursePage> pageList = new ArrayList<CoursePage>();
      for (Course main : courseList) {
          CoursePage vo = new CoursePage();
          BeanUtils.copyProperties(main, vo);
          List<CourseLesson> courseLessonList = courseLessonService.selectByMainId(main.getId());
          vo.setCourseLessonList(courseLessonList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "课程列表");
      mv.addObject(NormalExcelConstants.CLASS, CoursePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("课程数据", "导出人:"+sysUser.getRealname(), "课程"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<CoursePage> list = ExcelImportUtil.importExcel(file.getInputStream(), CoursePage.class, params);
              for (CoursePage page : list) {
                  Course po = new Course();
                  BeanUtils.copyProperties(page, po);
                  courseService.saveMain(po, page.getCourseLessonList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

}
