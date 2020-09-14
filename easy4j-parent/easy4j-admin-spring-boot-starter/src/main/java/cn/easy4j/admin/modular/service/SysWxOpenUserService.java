package cn.easy4j.admin.modular.service;

import cn.easy4j.admin.modular.entity.SysWxOpenUser;
import cn.easy4j.admin.modular.mapper.SysWxOpenUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * 微信用户表(SysWxOpenUser)表服务实现类
 *
 * @author yangzongmin
 * @since 2020-07-31 17:47
 */
@Service
public class SysWxOpenUserService extends ServiceImpl<SysWxOpenUserMapper, SysWxOpenUser> {

    public SysWxOpenUser selectByOpenId(String openId) {
        return this.lambdaQuery().eq(SysWxOpenUser::getOpenId, openId).one();
    }

    public void bindWechatByOpenId(@NonNull Long userId, @NonNull String openId) {
        this.lambdaUpdate().set(SysWxOpenUser::getUserId, userId).eq(SysWxOpenUser::getOpenId, openId).update();
    }

}
