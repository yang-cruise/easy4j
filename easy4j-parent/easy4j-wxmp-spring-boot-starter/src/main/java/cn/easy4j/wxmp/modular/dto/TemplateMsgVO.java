package cn.easy4j.wxmp.modular.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;

import java.util.List;

/**
 * @author ChenYichen
 * @since 2020/4/19
 */
@Data
public class TemplateMsgVO {

    /** 【必须】，接收消息的用户ID */
    @ApiModelProperty(value = "接收消息的用户ID")
    private Long wxMpUserId;

    /** 【必须】模板消息ID */
    @ApiModelProperty(value = "模板消息ID")
    private String templateId;

    /** 【必须】模板消息内容参数 */
    @ApiModelProperty(value = "模板消息内容参数")
    private List<WxMpTemplateData> data;

    /** [可选]，跳转网页链接 */
    @ApiModelProperty(value = "跳转网页链接")
    private String url;

    /** [可选]，小程序跳转页面路径，可带参数 */
    @ApiModelProperty(value = "小程序跳转页面路径，可带参数")
    private String wxMaPagePath;
}
