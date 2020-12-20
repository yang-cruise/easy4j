package cn.easy4j.oss.core.jackson;

import cn.easy4j.framework.util.ApplicationUtil;
import cn.easy4j.oss.core.storage.FileStorageStrategy;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典序列化工具
 *
 * @author yangzongmin
 * @since 2020/9/9 00:12
 */
public class JsonFileUrlConvertSerializer extends StdScalarSerializer<String> implements ContextualSerializer {

    private final String fieldName;

    public JsonFileUrlConvertSerializer() {
        super(String.class, false);
        this.fieldName = null;
    }

    public JsonFileUrlConvertSerializer(String fieldName) {
        super(String.class, false);
        this.fieldName = fieldName;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            JsonFileUrlConvert jsonFileUrlConvert = property.getAnnotation(JsonFileUrlConvert.class);
            if (jsonFileUrlConvert == null) {
                jsonFileUrlConvert = property.getContextAnnotation(JsonFileUrlConvert.class);
            }
            if (jsonFileUrlConvert != null) {
                boolean genFieldName = StringUtils.isBlank(jsonFileUrlConvert.fieldName()) || StringUtils.equals(jsonFileUrlConvert.fieldName(), property.getName());
                String newFiledName = genFieldName ? property.getName() + "FileUrl" : jsonFileUrlConvert.fieldName();
                return new JsonFileUrlConvertSerializer(newFiledName);
            }
        }
        return prov.findNullValueSerializer(property);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(value);
        List<String> fileUrls = new ArrayList<>();

        if (StringUtils.isBlank(value)) {
            gen.writeObjectField(this.fieldName, fileUrls);
            return;
        }

        FileStorageStrategy fileStorageStrategy = ApplicationUtil.getBean(FileStorageStrategy.class);
        if (StringUtils.indexOf(value, ",") > -1) {
            String[] fileNames = value.split(",");
            for (String fileName : fileNames) {
                if (StringUtils.isBlank(fileName)) {
                    continue;
                }
                fileUrls.add(fileStorageStrategy.getFileUrl(fileName));
            }
        } else {
            fileUrls.add(fileStorageStrategy.getFileUrl(value));
        }
        gen.writeObjectField(this.fieldName, fileUrls);
    }

}
