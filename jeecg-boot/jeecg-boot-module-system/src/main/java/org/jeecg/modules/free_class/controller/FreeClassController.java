package org.jeecg.modules.free_class.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.free_class.entity.FreeClass;
import org.jeecg.modules.free_class.service.IFreeClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 免费试听课程
 * @Author: jeecg-boot
 * @Date:   2019-11-30
 * @Version: V1.0
 */
@RestController
@RequestMapping("/free_class/freeClass")
@Slf4j
public class FreeClassController extends JeecgController<FreeClass, IFreeClassService> {
	@Autowired
	private IFreeClassService freeClassService;
	
	/**
	 * 分页列表查询
	 *
	 * @param freeClass
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FreeClass freeClass,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FreeClass> queryWrapper = QueryGenerator.initQueryWrapper(freeClass, req.getParameterMap());
		Page<FreeClass> page = new Page<FreeClass>(pageNo, pageSize);
		IPage<FreeClass> pageList = freeClassService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param freeClass
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FreeClass freeClass) {
		freeClassService.save(freeClass);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param freeClass
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FreeClass freeClass) {
		freeClassService.updateById(freeClass);
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
		freeClassService.removeById(id);
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
		this.freeClassService.removeByIds(Arrays.asList(ids.split(",")));
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
		FreeClass freeClass = freeClassService.getById(id);
		if(freeClass==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(freeClass);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param freeClass
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FreeClass freeClass) {
        return super.exportXls(request, freeClass, FreeClass.class, "免费试听课程");
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
        return super.importExcel(request, response, FreeClass.class);
    }

}
