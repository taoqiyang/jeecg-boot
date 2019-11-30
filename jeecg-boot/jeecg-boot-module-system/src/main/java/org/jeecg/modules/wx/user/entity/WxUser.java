package org.jeecg.modules.wx.user.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 微信用户表
 * @Author: jeecg-boot
 * @Date:   2019-11-29
 * @Version: V1.0
 */
@Data
@TableName("wx_user")
public class WxUser implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private String id;
	/**openId*/
	@Excel(name = "openId", width = 15)
    private String openId;
	/**呢称*/
	@Excel(name = "呢称", width = 15)
    private String nickName;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @Dict(dicCode = "gender")
    private Integer gender;
	/**城市*/
	@Excel(name = "城市", width = 15)
    private String city;
	/**省份*/
	@Excel(name = "省份", width = 15)
    private String province;
	/**国家*/
	@Excel(name = "国家", width = 15)
    private String country;
	/**头像地址*/
	@Excel(name = "头像地址", width = 15)
    private String avatarUrl;
	/**微信统一用户标识*/
	@Excel(name = "微信统一用户标识", width = 15)
    private String unionId;
    /**微信授权key*/
    private String sessionKey;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    private String mobile;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private String updateBy;
	/**会员级别*/
	@Excel(name = "会员级别", width = 15)
    @Dict(dicCode = "vipLevel")
    private Integer vipLevel;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
