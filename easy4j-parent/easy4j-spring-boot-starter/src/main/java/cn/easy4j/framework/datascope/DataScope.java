package cn.easy4j.framework.datascope;

import lombok.Data;

import java.util.Collections;
import java.util.Set;

/**
 * 数据范围
 *
 * @author ChenYichen
 */
@Data
public class DataScope {

    /**
     * 限制范围的字段名称
     */
    private String scopeName;

    /**
     * 具体的数据范围
     */
    private Set<Long> scopeIds;

    public DataScope() {
        new DataScope(Collections.emptySet());
    }

    public DataScope(Set<Long> scopeIds) {
        this(scopeIds, "dept_id");
    }

    public DataScope(Set<Long> scopeIds, String scopeName) {
        this.scopeIds = scopeIds;
        this.scopeName = scopeName;
    }

}
