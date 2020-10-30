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
 * (SysExceptionLog)表实体类
 *
 * @author yangzongmin
 * @since 2020-10-23 12:59:29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysExceptionLog对象", description = "")
public class SysExceptionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "请求IP")
    private String requestIp;

    @ApiModelProperty(value = "请求URI")
    private String requestUri;

    @ApiModelProperty(value = "请求方法")
    private String requestMethod;

    @ApiModelProperty(value = "请求参数")
    private String requestParams;

    @ApiModelProperty(value = "异常名称")
    private String exceptionName;

    @ApiModelProperty(value = "异常信息")
    private String exceptionMessage;

    @ApiModelProperty(value = "操作人ID")
    private Long operatorId;

    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

}