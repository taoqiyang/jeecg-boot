package org.jeecg.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.wx.user.entity.WxUser;
import org.jeecg.modules.wx.user.service.IWxUserService;
import org.jeecg.wx.config.WxMaConfiguration;
import org.jeecg.wx.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/user")
public class WxMaUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IWxUserService wxUserService;

    /**
     * 登陆接口
     */
    @PostMapping("/login")
    public Result<Object> login(@RequestBody Map<String, String> params) {

        String appid = params.get("appId");
        String code = params.get("code");
        String signature = params.get("signature");
        String rawData = params.get("rawData");
        String encryptedData = params.get("encryptedData");
        String iv = params.get("iv");

        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            String sessionKey = session.getSessionKey();
            // 用户信息校验
            if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
                return Result.error("校验失败");
            }
            WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
            WxUser source = new WxUser();
            source.setOpenId(userInfo.getOpenId());
            QueryWrapper<WxUser> queryWrapper = new QueryWrapper<>(source);
            WxUser wxUser = wxUserService.getOne(queryWrapper);
            if(wxUser == null) {
                //用户不存在 新建用户
                source.setNickName(userInfo.getNickName());
                source.setAvatarUrl(userInfo.getAvatarUrl());
                source.setGender(StringUtils.equalsIgnoreCase(userInfo.getGender(), "0") ? 0 : 1);
                source.setCountry(userInfo.getCountry());
                source.setProvince(userInfo.getProvince());
                source.setCity(userInfo.getCity());
                source.setSessionKey(sessionKey);
                source.setVipLevel(0);
                source.setCreateTime(new Date());
                wxUserService.save(source);
                wxUser = source;
            }else{
                //更新session_key
                source.setId(wxUser.getId());
                source.setSessionKey(sessionKey);
                wxUser.setSessionKey(sessionKey);
                wxUserService.updateById(source);
            }
            wxUser.setCreateTime(null);
            wxUser.setUpdateBy(null);
            wxUser.setUpdateTime(null);
            return Result.ok(wxUser);
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            return Result.error("系统异常");
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("/info")
    public String info(String appid, String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(userInfo);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    public String phone(String appid, String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(phoneNoInfo);
    }

}
