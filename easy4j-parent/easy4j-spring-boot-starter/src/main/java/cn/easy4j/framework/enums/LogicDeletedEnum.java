package cn.easy4j.framework.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author ChenYichen
 * @since 2020/3/11
 */
@AllArgsConstructor
@NoArgsConstructor
public enum LogicDeletedEnum implements IEnum<Integer> {

    /***已删除*/
    DELETED(1, "已删除"),

    /***未删除*/
    UN_DELETE(0, "未删除");

    private Integer value;
    private String desc;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
