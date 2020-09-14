package cn.easy4j.wxmp.modular.service.impl;

import cn.easy4j.wxmp.modular.entity.SysWxMp;
import cn.easy4j.wxmp.modular.mapper.SysWxMpMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 微信公众号配置表 服务类
 *
 * @author ChenYichen
 * @since 2020-03-20
 */
@Service
public class SysWxMpService extends ServiceImpl<SysWxMpMapper, SysWxMp> {

    public SysWxMp selectByAppId(String appId) {
        return this.lambdaQuery().eq(SysWxMp::getAppId, appId).one();
    }
}
