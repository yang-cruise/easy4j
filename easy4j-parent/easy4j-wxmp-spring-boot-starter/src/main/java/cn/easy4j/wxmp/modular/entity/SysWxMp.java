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
 * 微信公众号配置表
 * </p>
 *
 * @author ChenYichen
 * @since 2020-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysWxMp对象", description="微信公众号配置表")
public class SysWxMp implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "公众号类型")
    private Integer type;

    @ApiModelProperty(value = "公众号ID")
    private String appId;

    @ApiModelProperty(value = "公众号密钥")
    private String appSecret;

    @ApiModelProperty(value = "令牌")
    private String token;

    @ApiModelProperty(value = "消息加密密钥")
    private String aesKey;

    @ApiModelProperty(value = "公众号头像")
    private String avatar;

    @ApiModelProperty(value = "公众号二维码")
    private String mpQrcode;


}
