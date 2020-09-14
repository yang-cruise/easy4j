package cn.easy4j.wxmp.modular.service.impl;

import cn.easy4j.wxmp.modular.entity.SysWxMp;
import cn.easy4j.wxmp.modular.entity.SysWxMpUser;
import cn.easy4j.wxmp.modular.mapper.SysWxMpUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 微信用户表 服务类
 *
 * @author ChenYichen
 * @since 2020-03-23
 */
@Service
@Slf4j
public class SysWxMpUserService extends ServiceImpl<SysWxMpUserMapper, SysWxMpUser> {

    @Resource
    private SysWxMpService sysWxMpService;

    @Resource
    private WeixinMpService weixinMpService;

    public SysWxMpUser selectByOpenIdAndWxMpId(String openId, Long wxMpId) {
        return this.lambdaQuery().eq(SysWxMpUser::getOpenId, openId).eq(SysWxMpUser::getWxMpId, wxMpId).one();
    }

    /**
     * 根据unionId查询粉丝
     *
     * @param unionId unionId
     * @return 粉丝列表
     */
    public List<SysWxMpUser> selectByUnionId(String unionId) {
        return this.lambdaQuery().eq(SysWxMpUser::getUnionId, unionId).list();
    }

    public SysWxMpUser selectByOpenId(String openId) {
        return this.lambdaQuery().eq(SysWxMpUser::getOpenId, openId).one();
    }

    public SysWxMpUser saveOrUpdateByOpenId(String openId, String appId) {
        SysWxMp sysWxMpByAppId = sysWxMpService.selectByAppId(appId);

        SysWxMpUser sysWxMpUserForInsert = new SysWxMpUser();
        sysWxMpUserForInsert.setOpenId(openId);
        sysWxMpUserForInsert.setWxMpId(sysWxMpByAppId.getId());

        SysWxMpUser sysWxMpUserByOpenId = this.selectByOpenIdAndWxMpId(openId, sysWxMpByAppId.getId());
        if (Objects.isNull(sysWxMpUserByOpenId)) {
            this.save(sysWxMpUserForInsert);
        } else {
            sysWxMpUserForInsert.setId(sysWxMpUserByOpenId.getId());
            this.updateById(sysWxMpUserForInsert);
        }
        return this.getById(sysWxMpUserForInsert.getId());
    }

    public SysWxMpUser saveOrUpdateByWxUserInfo(WxMpUser userWxInfo, String appId) {
        SysWxMp sysWxMpByAppId = sysWxMpService.selectByAppId(appId);

        if (Objects.isNull(sysWxMpByAppId)) {
            return null;
        }

        Long sysWxMpUserId = this.saveOrUpdateByWxUserInfo(userWxInfo, sysWxMpByAppId.getId());
        return this.getById(sysWxMpUserId);
    }

    private Long saveOrUpdateByWxUserInfo(WxMpUser userWxInfo, Long sysWxMpId) {
        log.debug("保存更新的用户信息是：【{}】", userWxInfo);
        String openId = userWxInfo.getOpenId();

        SysWxMpUser sysWxMpUserForInsert = new SysWxMpUser();
        BeanUtils.copyProperties(userWxInfo, sysWxMpUserForInsert);
        sysWxMpUserForInsert.setWxMpId(sysWxMpId);
        if (null != userWxInfo.getSubscribe()) {
            sysWxMpUserForInsert.setSubscribe(userWxInfo.getSubscribe().equals(Boolean.TRUE) ? 1 : 0);
        }

        if (Objects.nonNull(userWxInfo.getSubscribeTime())) {
            sysWxMpUserForInsert.setSubscribeTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(userWxInfo.getSubscribeTime()), ZoneId.systemDefault()));
        }
        SysWxMpUser sysWxMpUserByOpenId = this.selectByOpenIdAndWxMpId(openId, sysWxMpId);

        if (Objects.isNull(sysWxMpUserByOpenId)) {
            this.save(sysWxMpUserForInsert);
        } else {
            sysWxMpUserForInsert.setId(sysWxMpUserByOpenId.getId());
            this.updateById(sysWxMpUserForInsert);
        }
        return sysWxMpUserForInsert.getId();
    }

    public Boolean isFirstSubscribe(String openId, String appId) {

        SysWxMpUser sysWxMpUser = baseMapper.selectByOpenIdAndAppId(openId, appId);

        if (Objects.isNull(sysWxMpUser) || sysWxMpUser.getSubscribe() == 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 根据openId改为未关注状态
     *
     * @param openId openId
     */
    public void updateUnsubscribeStatusByOpenId(String openId) {
        this.lambdaUpdate().eq(SysWxMpUser::getOpenId, openId).set(SysWxMpUser::getSubscribe, 0).update();
    }

    @Async
    public void downloadAllUsers(String appid) {
        WxMpService wxMpService = weixinMpService.getWxMpService(appid);

        String nextOpenid = null;
        while (true) {
            WxMpUserList wxMpUserList = getUserList(wxMpService, nextOpenid);
            if (Objects.isNull(wxMpUserList)) {
                break;
            }

            splitSaveOrUpdateWxMpUserList(wxMpService, wxMpUserList);
            nextOpenid = wxMpUserList.getNextOpenid();
        }
    }

    private void splitSaveOrUpdateWxMpUserList(WxMpService wxMpService, WxMpUserList wxMpUserList) {
        List<String> openidList = new ArrayList<>();
        for (int i = 0; i < wxMpUserList.getOpenids().size(); i++) {
            openidList.add(wxMpUserList.getOpenids().get(i));
            if (openidList.size() == 100) {
                batchSaveOrUpdateWxMpUserList(wxMpService, openidList);
                openidList = new ArrayList<>();
            }
        }
        if (CollectionUtils.isEmpty(openidList)) {
            return;
        }
        batchSaveOrUpdateWxMpUserList(wxMpService, openidList);
    }

    private void batchSaveOrUpdateWxMpUserList(WxMpService wxMpService, List<String> openidList) {
        try {
            List<WxMpUser> wxMpUserList = wxMpService.getUserService().userInfoList(openidList);
            if (CollectionUtils.isEmpty(wxMpUserList)) {
                return;
            }

            SysWxMp sysWxMpFromDb = sysWxMpService.selectByAppId(wxMpService.getWxMpConfigStorage().getAppId());
            for (WxMpUser wxMpUser : wxMpUserList) {
                this.saveOrUpdateByWxUserInfo(wxMpUser, sysWxMpFromDb.getId());
            }
        } catch (WxErrorException e) {
            log.error("批量获取用户基本信息失败：", e);
        }
    }

    private WxMpUserList getUserList(WxMpService wxMpService, String nextOpenid) {
        try {
            return wxMpService.getUserService().userList(nextOpenid);
        } catch (WxErrorException e) {
            log.error("获取用户列表失败：", e);
        }
        return null;
    }
}
