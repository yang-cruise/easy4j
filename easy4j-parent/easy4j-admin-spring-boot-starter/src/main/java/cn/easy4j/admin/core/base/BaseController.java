package cn.easy4j.admin.core.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * @author yangzongmin
 * @since 2019-08-14
 */
public abstract class BaseController {

    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    protected <T> IPage<T> getPage() {
        Page<T> page = new Page<>();
        String current = request.getParameter("current");
        if (StringUtils.isNotBlank(current)) {
            page.setCurrent(Integer.parseInt(current));
        }
        String size = request.getParameter("size");
        if (StringUtils.isNotBlank(size)) {
            int i = Integer.parseInt(size);
            page.setSize(Math.min(i, 100));
        }
        String column = request.getParameter("sort[column]");
        String asc = request.getParameter("sort[asc]");
        if (StringUtils.isNotBlank(column)) {
            column = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(column);
            page.setOrders(Collections.singletonList(Boolean.parseBoolean(asc) ? OrderItem.asc(column) : OrderItem.desc(column)));
        }
        return page;
    }
}
