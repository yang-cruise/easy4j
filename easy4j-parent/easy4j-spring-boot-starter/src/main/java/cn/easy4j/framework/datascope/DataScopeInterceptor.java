package cn.easy4j.framework.datascope;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据范围的拦截器
 *
 * @author ChenYichen
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();

        //查找参数中包含DataScope类型的参数
        DataScope dataScope = findDataScopeObject(parameterObject);
        if (dataScope == null) {
            return invocation.proceed();
        }

        String scopeName = dataScope.getScopeName();
        Set<Long> scopeIds = dataScope.getScopeIds();
        if (CollectionUtils.isEmpty(scopeIds)) {
            return invocation.proceed();
        }

        List<String> scopeIdList = scopeIds.parallelStream().map(String::valueOf).collect(Collectors.toList());
        String ids = String.join(",", scopeIdList);
        String targetSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + scopeName + " in (" + ids + ")";
        metaStatementHandler.setValue("delegate.boundSql.sql", targetSql);
        return invocation.proceed();
    }

    /**
     * 查找参数是否包括DataScope对象
     */
    public DataScope findDataScopeObject(Object parameterObject) {
        if (parameterObject instanceof DataScope) {
            return (DataScope) parameterObject;
        } else if (parameterObject instanceof Map) {
            for (Object val : ((Map<?, ?>) parameterObject).values()) {
                if (val instanceof DataScope) {
                    return (DataScope) val;
                }
            }
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

}
