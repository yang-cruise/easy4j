package cn.easy4j.dict.core.jackson;

import cn.easy4j.dict.modular.service.SysDictCacheService;
import cn.easy4j.framework.util.ApplicationUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * 数据字典序列化工具
 *
 * @author yangzongmin
 * @since 2020/6/2 17:07
 */
public class JsonDictConvertSerializer extends StdScalarSerializer<Object> implements ContextualSerializer {

    private final String code;

    private final String fieldName;

    public JsonDictConvertSerializer() {
        super(String.class, false);
        this.code = null;
        this.fieldName = null;
    }

    public JsonDictConvertSerializer(String code, String fieldName) {
        super(String.class, false);
        this.code = code;
        this.fieldName = fieldName;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            JsonDictConvert jsonDictConvert = property.getAnnotation(JsonDictConvert.class);
            if (jsonDictConvert == null) {
                jsonDictConvert = property.getContextAnnotation(JsonDictConvert.class);
            }
            if (jsonDictConvert != null) {
                boolean genFieldName = StringUtils.isBlank(jsonDictConvert.fieldName()) || StringUtils.equals(jsonDictConvert.fieldName(), property.getName());
                String newFiledName = genFieldName ? property.getName() + "Text" : jsonDictConvert.fieldName();
                return new JsonDictConvertSerializer(jsonDictConvert.code(), newFiledName);
            }
        }
        return prov.findNullValueSerializer(property);
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(value);
        if (Objects.nonNull(value)) {
            SysDictCacheService sysDictCacheService = ApplicationUtil.getBean(SysDictCacheService.class);
            String text = sysDictCacheService.selectSysDictItemByCodeAndKey(this.code, String.valueOf(value));
            gen.writeStringField(this.fieldName, text);
        }
    }

}
