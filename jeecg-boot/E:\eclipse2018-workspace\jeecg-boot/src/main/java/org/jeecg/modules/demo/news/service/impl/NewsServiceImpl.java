package org.jeecg.modules.demo.news.service.impl;

import org.jeecg.modules.demo.news.entity.News;
import org.jeecg.modules.demo.news.mapper.NewsMapper;
import org.jeecg.modules.demo.news.service.INewsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 新闻
 * @Author: jeecg-boot
 * @Date:   2019-11-14
 * @Version: V1.0
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

}
