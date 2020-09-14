package cn.easy4j.wxmp.modular.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 微信用户表
 * </p>
 *
 * @author ChenYichen
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysWxMpUser对象", description="微信用户表")
public class SysWxMpUser implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "微信公众号ID，关联sys_wx_mp.id")
    private Long wxMpId;

    @ApiModelProperty(value = "是否关注，0-未关注，1-已关注")
    private Integer subscribe;

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

    @ApiModelProperty(value = "公众号运营者对粉丝的备注")
    private String remark;

    @ApiModelProperty(value = "用户所在的分组ID")
    private Integer groupId;

    @ApiModelProperty(value = "二维码扫码场景")
    private String qrScene;

    @ApiModelProperty(value = "二维码扫码场景描述")
    private String qrSceneStr;

    @ApiModelProperty(value = "用户关注的渠道来源，ADD_SCENE_SEARCH-公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION-公众号迁移，ADD_SCENE_PROFILE_CARD-名片分享，ADD_SCENE_QR_CODE-扫描二维码，ADD_SCENE_PROFILE_ LINK-图文页内名称点击，ADD_SCENE_PROFILE_ITEM-图文页右上角菜单，ADD_SCENE_PAID-支付后关注，ADD_SCENE_OTHERS-其他")
    private String subscribeScene;

    @ApiModelProperty(value = "关注时间")
    private LocalDateTime subscribeTime;


}
