package org.jeecg.modules.news.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.news.entity.News;
import org.jeecg.modules.news.mapper.NewsMapper;
import org.jeecg.modules.news.service.INewsService;
import org.springframework.stereotype.Service;

/**
 * @Description: 新闻
 * @Author: jeecg-boot
 * @Date:   2019-11-14
 * @Version: V1.0
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

}
