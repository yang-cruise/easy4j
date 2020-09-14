package cn.easy4j.wxmp.modular.mapper;

import cn.easy4j.wxmp.modular.entity.SysWxMpUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 微信用户表 Mapper 接口
 *
 * @author ChenYichen
 * @since 2020-03-23
 */
@Mapper
public interface SysWxMpUserMapper extends BaseMapper<SysWxMpUser> {

    /**
     * 根据OpenId和AppId查询粉丝
     * @param openId openId
     * @param appId appId
     * @return 粉丝信息
     */
    @Select("select * from sys_wx_mp_user wmu join sys_wx_mp wm on wm.id = wmu.wx_mp_id  " +
            " where wmu.open_id = #{openId} and wm.app_id = #{appId}")
    SysWxMpUser selectByOpenIdAndAppId(@Param("openId") String openId, @Param("appId") String appId);
}
