package org.jeecg.modules.news.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.news.entity.News;
import org.jeecg.modules.news.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 新闻
 * @Author: jeecg-boot
 * @Date:   2019-11-14
 * @Version: V1.0
 */
@RestController
@RequestMapping("/news")
@Slf4j
public class NewsController extends JeecgController<News, INewsService> {
	@Autowired
	private INewsService newsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param news
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(News news,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<News> queryWrapper = QueryGenerator.initQueryWrapper(news, req.getParameterMap());
		Page<News> page = new Page<News>(pageNo, pageSize);
		IPage<News> pageList = newsService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param news
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody News news) {
		newsService.save(news);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param news
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody News news) {
		newsService.updateById(news);
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
		newsService.removeById(id);
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
		this.newsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		News news = newsService.getById(id);
		if(news==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(news);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param news
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, News news) {
        return super.exportXls(request, news, News.class, "新闻");
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
        return super.importExcel(request, response, News.class);
    }

}
