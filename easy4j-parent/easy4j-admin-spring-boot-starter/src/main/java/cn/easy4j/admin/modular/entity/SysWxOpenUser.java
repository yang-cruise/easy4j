package cn.easy4j.admin.modular.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 微信授权登录表
 *
 * @author yangzongmin
 * @since 2020/7/30 22:47
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SysWxOpenUser对象")
public class SysWxOpenUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "唯一标识")
    private String openId;

    @ApiModelProperty(value = "开放平台唯一标识")
    private String unionId;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别，0-未知，1-男，2-女")
    private Integer sex;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "语言")
    private String language;

    @ApiModelProperty(value = "头像")
    private String headImgUrl;

}
