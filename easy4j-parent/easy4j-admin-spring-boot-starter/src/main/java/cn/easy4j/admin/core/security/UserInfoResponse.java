package cn.easy4j.admin.core.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author yangzongmin
 * @since 2020/8/2 21:03
 */
@Setter
@Getter
@ToString
public class UserInfoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("openid")
    private String openId;

    @JsonProperty("unionid")
    private String unionId;

    private String nickname;

    private Integer sex;

    private String country;

    private String province;

    private String city;

    private String language;

    @JsonProperty("headimgurl")
    private String headImgUrl;

}
