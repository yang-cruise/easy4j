package cn.easy4j.framework.db;

import cn.easy4j.common.exception.ServiceException;
import cn.easy4j.framework.util.JacksonUtil;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库初始化，可初始化表，校验字段，校验表名是否存在等
 * 注意：实现类必须添加@Component注解
 * @author yangzongmin
 * @since 2019-07-19
 */
@Slf4j
public abstract class BaseDbInitializer {

    /**
     * 初始化数据库
     */
    public void dbInit() {
        initTable();
        fieldsValidate();
    }

    /**
     * 初始化表结构
     */
    private void initTable() {

        // 校验参数
        String tableName = this.getTableName();
        String tableInitSql = this.getTableInitSql();
        if (StringUtils.isAnyBlank(tableName, tableInitSql)) {
            throw new ServiceException("初始化数据库，参数错误");
        }

        // 列出数据库中所有的表
        List<Object> tableLists = SqlRunner.db().selectObjs("SHOW TABLES");

        // 判断数据库中是否有这张表，如果没有就初始化
        if (!tableLists.contains(tableName.toUpperCase()) && !tableLists.contains(tableName.toLowerCase())) {
            SqlRunner.db().update(tableInitSql);
            log.info("{} 表结构创建成功", getTableName());

            // 插入初始数据
            List<String> dataInitSql = this.getDataInitSql();

            if (!CollectionUtils.isEmpty(dataInitSql)) {
                for (String sql : dataInitSql) {
                    SqlRunner.db().insert(sql);
                }
                log.info("{} 初始数据插入成功", getTableName());
            }

        }
    }

    /**
     * 校验实体和对应表结构是否有不一致的
     */
    private void fieldsValidate() {

        //校验参数
        String sql = this.showColumnsSql();
        if (StringUtils.isBlank(sql)) {
            throw new ServiceException("查询表字段，参数错误");
        }

        //检查数据库中的字段，是否和实体字段一致
        List<Map<String, Object>> tableFields = SqlRunner.db().selectList(sql);
        if (tableFields != null && !tableFields.isEmpty()) {

            //用于保存实体中不存在的字段的名称集合
            List<String> fieldsNotInClass = new ArrayList<>();

            //反射获取字段的所有字段名称，包含父类的所有字段
            List<String> classFields = this.getClassFields();
            for (Map<String, Object> tableField : tableFields) {
                String fieldName = (String) tableField.get("Field");
                if (!classFields.contains(fieldName.toLowerCase())) {
                    fieldsNotInClass.add(fieldName);
                }
            }
            //如果集合不为空，代表有实体和数据库不一致的数据
            if (!fieldsNotInClass.isEmpty()) {
                throw new ServiceException("表名:【" + this.getTableName() + "】数据库字段与实体字段不一致, 不一致的字段如下：【" + JacksonUtil.toJson(fieldsNotInClass) + "】, 【" + JacksonUtil.toJson(classFields) + "】");
            }
        }
    }

    /**
     * 反射获取字段的所有字段名称，包含父类的所有字段
     * @return 所有字段名，包含父类字段
     */
    private List<String> getClassFields() {
        ArrayList<String> filedNamesUnderlineCase = new ArrayList<>();
        Class<?> entityClass = this.getEntityClass();
        while (entityClass != null) {
            Field[] declaredFields = entityClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                String fieldName = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(declaredField.getName());
                filedNamesUnderlineCase.add(fieldName);
            }
            entityClass = entityClass.getSuperclass();
        }
        return filedNamesUnderlineCase;
    }

    /**
     * 获取表的字段
     * @return 获取表字段SQL语句
     */
    private String showColumnsSql() {
        return "SHOW COLUMNS FROM " + this.getTableName();
    }

    /**
     * 获得创建表结构脚本
     *
     * @return sql
     */
    protected abstract String getTableInitSql();

    /**
     * 获得初始数据脚本
     *
     * @return sql
     */
    protected abstract List<String> getDataInitSql();

    /**
     * 获得表名
     *
     * @return 表名
     */
    protected abstract String getTableName();

    /**
     * 获得表对应的实体
     *
     * @return 实体
     */
    protected abstract Class<?> getEntityClass();
}
