package cn.easy4j.admin.modular.mapper;

import cn.easy4j.admin.modular.entity.SysWxOpenUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信用户表(SysWxOpenUser)表数据库访问层
 *
 * @author yangzongmin
 * @since 2020-07-31 17:47
 */
@Mapper
public interface SysWxOpenUserMapper extends BaseMapper<SysWxOpenUser> {

}
