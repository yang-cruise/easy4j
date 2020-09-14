package cn.easy4j.framework.jackson;

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
 * @author yangzongmin
 * @since 2020/6/1 22:11
 */
public class JsonMaskSerializer extends StdScalarSerializer<String> implements ContextualSerializer {

    private final JsonMaskEnum type;

    public JsonMaskSerializer() {
        super(String.class, false);
        this.type = null;
    }

    public JsonMaskSerializer(JsonMaskEnum type) {
        super(String.class, false);
        this.type = type;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null && String.class.isAssignableFrom(property.getType().getRawClass())) {
            JsonMask jsonMask = property.getAnnotation(JsonMask.class);
            if (jsonMask == null) {
                jsonMask = property.getContextAnnotation(JsonMask.class);
            }
            if (jsonMask != null) {
                return new JsonMaskSerializer(jsonMask.type());
            }
        }
        return prov.findNullValueSerializer(property);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (Objects.isNull(this.type)) {
            gen.writeString(value);
            return;
        }

        switch (this.type) {
            case MOBILE:
                gen.writeString(getMaskMobile(value));
                break;
            case EMAIL:
                gen.writeString(getMaskEmail(value));
                break;
            case ID_CARD:
                gen.writeString(getMaskIdCard(value));
                break;
            case ADDRESS:
                gen.writeString(getMaskAddress(value));
                break;
            case BANK_CARD:
                gen.writeString(getMaskBankCard(value));
                break;
            default: gen.writeString(value);
        }
    }

    private String getMaskAddress(String address) {
        if (StringUtils.isBlank(address)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.trim(address).replaceAll("([\\s\\S]{3})[\\s\\S]+([\\s\\S]{3})","$1********$2");
    }

    private String getMaskBankCard(String bankCard) {
        if (StringUtils.isBlank(bankCard)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.trim(bankCard).replaceAll("(\\d{4})\\d+(\\d{4})","$1******$2");
    }

    /**
     * 获得脱敏邮箱地址
     * @param email 原邮箱地址
     * @return 脱敏后邮箱地址，例：tes****@qq.com
     */
    private String getMaskEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return StringUtils.EMPTY;
        }
        String at = "@";
        if (!StringUtils.contains(email, at)) {
            return email;
        }
        String str = StringUtils.trim(email);
        int length = StringUtils.indexOf(str, at);
        String content = StringUtils.substring(email, 0, length);
        String mask = StringUtils.overlay(content, "****", 3, content.length());
        return mask + StringUtils.substring(str, length);
    }

    /**
     * 获得脱敏手机号
     * @param mobile 原手机号
     * @return 脱敏后手机号，例：138****8000
     */
    private String getMaskMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.trim(mobile).replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }

    /**
     * 获得脱敏身份证号
     * @param idCard 原身份证号
     * @return 脱敏后身份证号，例：510********5052，510********505X
     */
    private String getMaskIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.trim(idCard).replaceAll("(\\d{3})\\d+(\\d{3}[0-9Xx])","$1********$2");
    }

}
