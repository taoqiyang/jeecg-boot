package org.jeecg.modules.wx.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.wx.user.entity.WxUser;
import org.jeecg.modules.wx.user.mapper.WxUserMapper;
import org.jeecg.modules.wx.user.service.IWxUserService;
import org.springframework.stereotype.Service;


/**
 * @Description: 微信用户表
 * @Author: jeecg-boot
 * @Date:   2019-11-29
 * @Version: V1.0
 */
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements IWxUserService {

}
