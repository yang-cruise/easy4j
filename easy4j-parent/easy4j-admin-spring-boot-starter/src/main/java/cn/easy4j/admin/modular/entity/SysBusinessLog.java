package cn.easy4j.admin.modular.entity;

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
 * (SysBusinessLog)表实体类
 *
 * @author yangzongmin
 * @since 2020-10-23 12:58:05
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysBusinessLog对象", description = "")
public class SysBusinessLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "功能模块")
    private String modular;

    @ApiModelProperty(value = "业务ID")
    private String entityId;

    @ApiModelProperty(value = "请求IP")
    private String requestIp;

    @ApiModelProperty(value = "请求URI")
    private String requestUri;

    @ApiModelProperty(value = "请求方法")
    private String requestMethod;

    @ApiModelProperty(value = "请求参数")
    private String requestParams;

    @ApiModelProperty(value = "响应参数")
    private String responseParams;

    @ApiModelProperty(value = "变更详细")
    private String changeDetails;

    @ApiModelProperty(value = "操作人ID")
    private Long operatorId;

    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

}