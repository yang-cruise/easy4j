package cn.easy4j.dict.modular.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yangzongmin
 * @since 2019-08-21
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "SysDictItem对象")
public class SysDictItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "字典ID，关联sys_dict.id")
    private Long dictId;

    @ApiModelProperty(value = "字典值")
    @TableField(value = "`key`")
    private String key;

    @ApiModelProperty(value = "显示内容")
    @TableField(value = "`value`")
    private String value;

    @ApiModelProperty(value = "显示顺序")
    private Integer sort;

    @TableLogic
    @JsonIgnore
    private Boolean isDeleted;

}
