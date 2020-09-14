package cn.easy4j.admin.core.security;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * @author yangzongmin
 * @since 2020-08-04 23:44
 */
public class NotBindWechatException extends AuthenticationException {

	@Getter
	private final UserInfoResponse userInfo;

	public NotBindWechatException(String msg, UserInfoResponse userInfo) {
		super(msg);
		this.userInfo = userInfo;
	}

}
